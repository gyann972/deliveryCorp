/**
 * 
 */
package yannis.technical.challenge.deliveryCorp.repository.interfaces;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import yannis.technical.challenge.deliveryCorp.model.Offer;
import yannis.technical.challenge.deliveryCorp.util.TestUtil;

/**
 * @author YaYa
 *
 */
@RunWith(SpringRunner.class)
@DataJpaTest
public class RepositoryTest {

	@Autowired
	private TestEntityManager entityManager;

	@Autowired
	private OfferRepository repository;

	@Test
	public void testGetOfferById() throws Exception {

		Offer offer = TestUtil.getOfferStateAvailable();
		this.entityManager.persist(offer);
		Optional<Offer> offerFound = this.repository.findById(offer.getId());
		assertEquals(offer, offerFound.get());
	}
	
	@Test
	public void testGetSeveralOffers() throws Exception {

		Offer offer = TestUtil.getOfferStateAvailable();
		Offer offerExpired = TestUtil.getOfferStateExprired();
		this.entityManager.persist(offer);
		this.entityManager.persist(offerExpired);
		
		List<Offer> offersFound = this.repository.findAll();
		List<Offer> offersExpected = Arrays.asList(offer, offerExpired);
		
		assertEquals(offersExpected, offersFound);
	}

}
