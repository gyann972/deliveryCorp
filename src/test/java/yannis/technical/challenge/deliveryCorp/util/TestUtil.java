/**
 * 
 */
package yannis.technical.challenge.deliveryCorp.util;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import yannis.technical.challenge.deliveryCorp.constants.State;
import yannis.technical.challenge.deliveryCorp.model.Offer;

/**
 * @author YaYa
 *
 */
public class TestUtil {

	public static Offer getOfferStateAvailable() {
		return createOfferWithState(State.AVAILABLE);
	}

	public static Offer getOfferStateExprired() {
		return createOfferWithState(State.EXPIRED);
	}

	public static Offer getOfferStateCancelled() {
		return createOfferWithState(State.CANCELLED);
	}

	private static Offer createOfferWithState(State cancelled) {
		return new Offer("offer", cancelled, "little description", BigDecimal.ONE, "£");
	}

	public static List<Offer> createListOffers() {
		return Arrays.asList(getOfferStateAvailable(), getOfferStateExprired());
	}

	public static String objectToJson(Object object) throws JsonProcessingException {
		ObjectMapper objectMapper = new ObjectMapper();
		return objectMapper.writeValueAsString(object);
	}
}
