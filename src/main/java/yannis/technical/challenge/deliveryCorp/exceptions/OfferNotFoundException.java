/**
 * 
 */
package yannis.technical.challenge.deliveryCorp.exceptions;

/**
 * @author YaYa
 *
 */
public class OfferNotFoundException extends RuntimeException{

	private static final long serialVersionUID = 3451031324896110746L;

	public OfferNotFoundException(Long id) {
		super("Offer with id " + id + " wasn't found");
	}
	
}
