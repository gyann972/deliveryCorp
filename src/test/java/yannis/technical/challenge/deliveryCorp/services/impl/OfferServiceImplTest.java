/**
 * 
 */
package yannis.technical.challenge.deliveryCorp.services.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.util.CollectionUtils;

import yannis.technical.challenge.deliveryCorp.constants.State;
import yannis.technical.challenge.deliveryCorp.exceptions.OfferNotFoundException;
import yannis.technical.challenge.deliveryCorp.model.Offer;
import yannis.technical.challenge.deliveryCorp.repository.interfaces.OfferRepository;
import yannis.technical.challenge.deliveryCorp.util.TestUtil;

/**
 * @author YaYa
 *
 */
@RunWith(MockitoJUnitRunner.class)
public class OfferServiceImplTest {

	@InjectMocks
	private OfferServiceImpl offerService;

	@Mock
	private OfferRepository repository;

	private Offer offerStateAvailable;
	
	private Optional<Offer> offerAvailableOptional;

	public OfferServiceImplTest() {
		offerStateAvailable = TestUtil.getOfferStateAvailable();
		offerStateAvailable.setId(1l);
	}

	@Before
	public void setUp() throws Exception {
		Mockito.reset(repository);
		offerAvailableOptional = getOtptionalOffer(offerStateAvailable);
	}

	@Test
	public void testGetOfferNominalCase() {
		Mockito.when(repository.findById(Mockito.any(Long.class))).thenReturn(offerAvailableOptional);

		Offer offerCreated = offerService.getOffer(offerStateAvailable.getId());

		assertEquals(offerStateAvailable, offerCreated);

		Mockito.verify(repository, Mockito.times(1)).findById(Mockito.any(Long.class));
		Mockito.verifyNoMoreInteractions(repository);
	}

	@Test
	public void testGetOfferParamNull() {
		assertNull(offerService.getOffer(null));
	}

	@Test
	public void testCreateOfferNominalCase() {
		Mockito.when(repository.save(Mockito.any(Offer.class))).thenReturn(offerStateAvailable);

		Offer offerCreated = offerService.createOffer(offerStateAvailable);

		assertEquals(offerStateAvailable, offerCreated);

		Mockito.verify(repository, Mockito.times(1)).save(Mockito.any(Offer.class));
		Mockito.verifyNoMoreInteractions(repository);
	}

	@Test
	public void testCreateOfferParamNull() {
		assertNull(offerService.createOffer(null));
	}

	@Test
	public void testGetAllOffersNominalCase() {

		List<Offer> expectedListOffers = TestUtil.createListOffers();

		Mockito.when(repository.findAll()).thenReturn(expectedListOffers);

		List<Offer> obtainedListOffers = offerService.getAllOffers();

		assertFalse(CollectionUtils.isEmpty(obtainedListOffers));
		Offer offer = obtainedListOffers.get(0);
		assertEquals(State.AVAILABLE, offer.getState());

		Mockito.verify(repository, Mockito.times(1)).findAll();
		Mockito.verifyNoMoreInteractions(repository);
	}

	@Test
	public void testCancelOfferNominalCase() {

		Mockito.when(repository.findById(Mockito.any(Long.class))).thenReturn(offerAvailableOptional);

		Mockito.when(repository.save(Mockito.any(Offer.class))).thenReturn(offerStateAvailable);

		Offer offerCreated = offerService.cancelOffer(offerStateAvailable.getId());

		assertNotNull(offerCreated);
		assertEquals(State.CANCELLED, offerCreated.getState());
		assertEquals(offerStateAvailable.getId(), offerCreated.getId());
		assertEquals(offerStateAvailable.getName(), offerCreated.getName());
		assertEquals(offerStateAvailable.getCurrency(), offerCreated.getCurrency());
		assertEquals(offerStateAvailable.getDescription(), offerCreated.getDescription());

		Mockito.verify(repository, Mockito.times(1)).save(Mockito.any(Offer.class));
		Mockito.verify(repository, Mockito.times(1)).findById(Mockito.any(Long.class));
		Mockito.verifyNoMoreInteractions(repository);
	}

	@Test(expected=OfferNotFoundException.class)
	public void testCancelOfferOfferNotFoundException() {

		long idAlreadyCancelled = 2L;
		Mockito.when(repository.findById(Mockito.any(Long.class))).thenThrow(new OfferNotFoundException(idAlreadyCancelled));

		offerService.cancelOffer(idAlreadyCancelled);

//		assertNotNull(offerCreated);
//		assertEquals(State.CANCELLED, offerCreated.getState());
//		assertEquals(offerStateAvailable.getId(), offerCreated.getId());
//		assertEquals(offerStateAvailable.getName(), offerCreated.getName());
//		assertEquals(offerStateAvailable.getCurrency(), offerCreated.getCurrency());
//		assertEquals(offerStateAvailable.getDescription(), offerCreated.getDescription());
//
//		Mockito.verify(repository, Mockito.times(1)).save(Mockito.any(Offer.class));
		Mockito.verify(repository, Mockito.times(1)).findById(Mockito.any(Long.class));
		Mockito.verifyNoMoreInteractions(repository);
	}

	@Test
	public void testCancelOfferParamNull() {
		assertNull(offerService.cancelOffer(null));
	}

	public Optional<Offer> getOtptionalOffer(Offer offer) {
		return Optional.of(offer);
	}
}
