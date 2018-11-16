/**
 * 
 */
package yannis.technical.challenge.deliveryCorp.controller;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import yannis.technical.challenge.deliveryCorp.media.assembler.OfferResourceAssembler;
import yannis.technical.challenge.deliveryCorp.model.Offer;
import yannis.technical.challenge.deliveryCorp.services.interfaces.OfferServiceInterface;

/**
 * @author YaYa
 *
 */
@RequestMapping("/deliveryCorp")
@RestController
public class OfferController {

	@Autowired
	private OfferResourceAssembler assembler;

	@Autowired
	@Qualifier(value = "OfferServiceImpl")
	private OfferServiceInterface offerService;

	@GetMapping("/offer/{id}")
	public ResponseEntity<?> getOffer(@PathVariable Long id) {
		Offer offer = offerService.getOffer(id);

		Resource<Offer> resource = assembler.toResource(offer);

		return ResponseEntity.ok(resource);
	}

	@PostMapping("/createOffer")
	public ResponseEntity<?> createOffer(@Valid @RequestBody Offer offer) {

		Offer offerCreated = offerService.createOffer(offer);

		assembler.toResource(offerCreated);

		return ResponseEntity.created(linkTo(methodOn(OfferController.class).getOffer(offerCreated.getId())).toUri())
				.body(assembler.toResource(offerCreated));
	}

	@GetMapping("/offers")
	public ResponseEntity<?> getAllOffers() {
		List<Resource<Offer>> listOrdersResources = offerService.getAllOffers().stream().map(assembler::toResource)
				.collect(Collectors.toList());

		Resources<Resource<Offer>> resources = new Resources<>(listOrdersResources,
				linkTo(methodOn(OfferController.class).getAllOffers()).withSelfRel());

		return ResponseEntity.ok(resources);
	}

	@PutMapping("/cancelOffer/{id}")
	public ResponseEntity<?> cancelOffer(@PathVariable Long id) {

		Offer cancelOffer = offerService.cancelOffer(id);

		Resource<Offer> resource = assembler.toResource(cancelOffer);

		return ResponseEntity.ok(resource);
	}
}
