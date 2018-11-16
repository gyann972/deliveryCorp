package yannis.technical.challenge.deliveryCorp.controller;

import static org.hamcrest.Matchers.equalTo;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import yannis.technical.challenge.deliveryCorp.exceptions.OfferNotFoundException;
import yannis.technical.challenge.deliveryCorp.model.Offer;
import yannis.technical.challenge.deliveryCorp.services.interfaces.OfferServiceInterface;
import yannis.technical.challenge.deliveryCorp.util.TestUtil;

/**
 * @author YaYa
 *
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class OfferControllerTest {

	private static final String DELIVERY_CORP = "/deliveryCorp";
	
	private static final String DELIVERY_CORP_CANCEL_OFFER = DELIVERY_CORP + "/cancelOffer/{id}";

	private static final String DELIVERY_CORP_OFFER = DELIVERY_CORP + "/offer/{id}";

	private static final String DELIVERY_CORP_OFFERS = DELIVERY_CORP + "/offers";

	private static final String DELIVERY_CORP_CREATE_OFFER = DELIVERY_CORP + "/createOffer";

	private static final String HTTP_LOCALHOST = "http://localhost:";

	@LocalServerPort
	private int port;

	// @Value("${local.management.port}")
	// private int mgt;

	@Autowired
	private MockMvc mvc;

	@MockBean
	private OfferServiceInterface service;

	private Offer offerAvailable;

	private Offer offerCancelled;

	public OfferControllerTest() {
		super();
		offerAvailable = TestUtil.getOfferStateAvailable();
		offerCancelled = TestUtil.getOfferStateCancelled();

		AtomicLong atomicLong = new AtomicLong();
		offerAvailable.setId(atomicLong.incrementAndGet());
		offerCancelled.setId(offerAvailable.getId());
	}

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		Mockito.reset(service);
	}

	@Test
	public void createOfferNominalCase() throws Exception {

		Mockito.when(service.createOffer(Mockito.any(Offer.class))).thenReturn(offerAvailable);

		ResultActions resultActions = mvc.perform(MockMvcRequestBuilders
				.post(HTTP_LOCALHOST + this.port + DELIVERY_CORP_CREATE_OFFER).accept(MediaType.APPLICATION_JSON_UTF8)
				.contentType(MediaType.APPLICATION_JSON_UTF8).content(TestUtil.objectToJson(offerAvailable)))
				.andExpect(status().isCreated());

		verifyResult(offerAvailable, resultActions);

		Mockito.verify(service, Mockito.times(1)).createOffer(Mockito.any(Offer.class));
		Mockito.verifyNoMoreInteractions(service);
	}

	@Test
	public void createOfferRequestEmpty() throws Exception {
		mvc.perform(MockMvcRequestBuilders.post(HTTP_LOCALHOST + this.port + DELIVERY_CORP_CREATE_OFFER)
				.accept(MediaType.APPLICATION_JSON_UTF8).contentType(MediaType.APPLICATION_JSON_UTF8).content(""))
				.andExpect(status().is4xxClientError());
	}

	@Test
	public void getAnOfferNominalCase() throws Exception {
		Mockito.when(service.getOffer(Mockito.any(Long.class))).thenReturn(offerAvailable);

		ResultActions resultActions = mvc.perform(
				MockMvcRequestBuilders.get(HTTP_LOCALHOST + this.port + DELIVERY_CORP_OFFER, offerAvailable.getId())
						.accept(MediaType.APPLICATION_JSON_UTF8).contentType(MediaType.APPLICATION_JSON_UTF8)
						.content(TestUtil.objectToJson(offerAvailable)))
				.andExpect(status().isOk());

		verifyResult(offerAvailable, resultActions);

		Mockito.verify(service, Mockito.times(1)).getOffer(Mockito.any(Long.class));
		Mockito.verifyNoMoreInteractions(service);
	}

	@Test
	public void getAnOfferInexisting() throws Exception {
		long inexistingId = 5L;
		Mockito.when(service.getOffer(Mockito.any(Long.class))).thenThrow(new OfferNotFoundException(inexistingId));

		mvc.perform(MockMvcRequestBuilders.get(HTTP_LOCALHOST + this.port + DELIVERY_CORP_OFFER, inexistingId))
				.andExpect(status().isNotFound())
				.andExpect(content().string(equalTo("Offer with id " + inexistingId + " wasn't found")));

		Mockito.verify(service, Mockito.times(1)).getOffer(Mockito.any(Long.class));
		Mockito.verifyNoMoreInteractions(service);
	}

	@Test
	public void getAllOffersNominalCase() throws Exception {

		List<Offer> listOffers = Arrays.asList(offerAvailable);
		Mockito.when(service.getAllOffers()).thenReturn(listOffers);

		ResultActions resultActions = mvc
				.perform(MockMvcRequestBuilders.get(HTTP_LOCALHOST + this.port + DELIVERY_CORP_OFFERS))
				.andExpect(status().isOk());

		resultActions.andExpect(jsonPath("$._embedded.offerList[0].id", Matchers.is(offerAvailable.getId().intValue())))
				.andExpect(jsonPath("$._embedded.offerList[0].name", Matchers.is(offerAvailable.getName()))).andExpect(
						jsonPath("$._embedded.offerList[0].state", Matchers.is(offerAvailable.getState().toString())));

		Mockito.verify(service, Mockito.times(1)).getAllOffers();
		Mockito.verifyNoMoreInteractions(service);

	}

	@Test
	public void getAllOffersEmpty() throws Exception {

		List<Offer> listOffersEmpty = new ArrayList<>();
		Mockito.when(service.getAllOffers()).thenReturn(listOffersEmpty);

		mvc.perform(MockMvcRequestBuilders.get(HTTP_LOCALHOST + this.port + DELIVERY_CORP_OFFERS))
				.andExpect(status().isOk()).andExpect(jsonPath("$..offerList", Matchers.empty()));

		Mockito.verify(service, Mockito.times(1)).getAllOffers();
		Mockito.verifyNoMoreInteractions(service);

	}

	@Test
	public void cancelOfferNominalCase() throws Exception {

		Mockito.when(service.cancelOffer(Mockito.any(Long.class))).thenReturn(offerCancelled);

		ResultActions resultActions = mvc.perform(MockMvcRequestBuilders
				.put(HTTP_LOCALHOST + this.port + DELIVERY_CORP_CANCEL_OFFER, offerAvailable.getId()))
				.andExpect(status().isOk());

		verifyResult(offerCancelled, resultActions);

		Mockito.verify(service, Mockito.times(1)).cancelOffer(Mockito.any(Long.class));
		Mockito.verifyNoMoreInteractions(service);
	}

	@Test
	public void cancelOfferAlreadyCancelled() throws Exception {

		Long idOfferCancelled = offerCancelled.getId();
		Mockito.when(service.cancelOffer(Mockito.any(Long.class)))
				.thenThrow(new OfferNotFoundException(idOfferCancelled));

		mvc.perform(
				MockMvcRequestBuilders.put(HTTP_LOCALHOST + this.port + DELIVERY_CORP_CANCEL_OFFER, idOfferCancelled))
				.andExpect(status().isNotFound())
				.andExpect(content().string(equalTo("Offer with id " + idOfferCancelled + " wasn't found")));

		Mockito.verify(service, Mockito.times(1)).cancelOffer(Mockito.any(Long.class));
		Mockito.verifyNoMoreInteractions(service);
	}

	public void verifyResult(Offer expectedOffer, ResultActions resultActions) throws Exception {
		resultActions.andExpect(jsonPath("$.id", Matchers.is(expectedOffer.getId().intValue())))
				.andExpect(jsonPath("$.name", Matchers.is(expectedOffer.getName())))
				.andExpect(jsonPath("$.state", Matchers.is(expectedOffer.getState().toString())));
	}
}
