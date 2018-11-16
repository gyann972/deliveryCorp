/**
 * 
 */
package yannis.technical.challenge.deliveryCorp.media.assembler;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;
import org.springframework.hateoas.Resource;

import yannis.technical.challenge.deliveryCorp.model.Offer;

/**
 * @author YaYa
 *
 */
public class OfferResourceAssemblerTest {

	OfferResourceAssembler assembler = new OfferResourceAssembler();

	@Test
	public void testToResourceOK() {
		Offer offer = new Offer();
		Resource<Offer> resource = assembler.toResource(offer);
		assertNotNull(resource);
		
		assertEquals(offer, resource.getContent());
	}

	@Test
	public void testToResourceNull() {
		assertNull(assembler.toResource(null));
	}

}
