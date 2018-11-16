package yannis.technical.challenge.deliveryCorp.media.assembler;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import org.springframework.hateoas.Resource;
import org.springframework.hateoas.ResourceAssembler;
import org.springframework.stereotype.Component;

import yannis.technical.challenge.deliveryCorp.controller.OfferController;
import yannis.technical.challenge.deliveryCorp.model.Offer;

@Component
public class OfferResourceAssembler implements ResourceAssembler<Offer, Resource<Offer>> {

	public OfferResourceAssembler() {
		super();
	}

	@Override
	public Resource<Offer> toResource(Offer offer) {

		Resource<Offer> orderResource = null;

		if (offer != null) {
			orderResource = new Resource<>(offer,
					linkTo(methodOn(OfferController.class).getOffer(offer.getId())).withSelfRel(),
					linkTo(methodOn(OfferController.class).getAllOffers()).withRel("offers"));
		}

		return orderResource;
	}

}
