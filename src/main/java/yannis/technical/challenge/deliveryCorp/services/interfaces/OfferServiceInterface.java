/**
 * 
 */
package yannis.technical.challenge.deliveryCorp.services.interfaces;

import java.util.List;

import yannis.technical.challenge.deliveryCorp.model.Offer;

/**
 * @author YaYa
 *
 */
public interface OfferServiceInterface {

	/**
	 * @param id
	 * @return
	 */
	public Offer getOffer(Long id);

	/**
	 * Create a new offer
	 * 
	 * @param offer
	 *            The offer to created
	 * @return Offer the created
	 */
	public Offer createOffer(Offer offer);

	/**
	 * Only return offers with state = AVAILABLE
	 * 
	 * @return List<Offer> The offers available
	 */
	public List<Offer> getAllOffers();

	/**
	 * @param id
	 * @return
	 */
	public Offer cancelOffer(Long id);
}
