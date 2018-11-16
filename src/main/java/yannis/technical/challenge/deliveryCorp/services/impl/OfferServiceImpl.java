/**
 * 
 */
package yannis.technical.challenge.deliveryCorp.services.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import yannis.technical.challenge.deliveryCorp.constants.State;
import yannis.technical.challenge.deliveryCorp.exceptions.OfferNotFoundException;
import yannis.technical.challenge.deliveryCorp.model.Offer;
import yannis.technical.challenge.deliveryCorp.repository.interfaces.OfferRepository;
import yannis.technical.challenge.deliveryCorp.services.interfaces.OfferServiceInterface;

/**
 * @author YaYa
 *
 */
@Service(value = "OfferServiceImpl")
public class OfferServiceImpl implements OfferServiceInterface {

	@Autowired
	private OfferRepository repository;

	public OfferServiceImpl() {
		super();
	}

	@Override
	public Offer createOffer(Offer offer) {
		// TODO Auto-generated method stub
		Offer offerCreated = null;

		if (offer != null) {
			offerCreated = repository.save(offer);
		}

		return offerCreated;
	}

	@Override
	public List<Offer> getAllOffers() {
		return repository.findAll().stream().filter(o -> State.AVAILABLE.equals(o.getState()))
				.collect(Collectors.toList());
	}

	@Override
	public Offer cancelOffer(Long id) {
		Offer offerToCancelled = getOffer(id);

		Offer offerCancelled = null;

		if (offerToCancelled != null && State.AVAILABLE.equals(offerToCancelled.getState())) {
			offerToCancelled.setState(State.CANCELLED);
			offerCancelled = repository.save(offerToCancelled);
		}

		return offerCancelled;
	}

	@Override
	public Offer getOffer(Long id) {
		Offer offerReturned = null;

		if (id != null) {
			offerReturned = repository.findById(id).filter(o -> State.AVAILABLE.equals(o.getState()))
					.orElseThrow(() -> new OfferNotFoundException(id));
		}
		return offerReturned;
	}

}
