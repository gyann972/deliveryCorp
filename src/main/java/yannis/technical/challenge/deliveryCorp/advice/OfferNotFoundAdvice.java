/**
 * 
 */
package yannis.technical.challenge.deliveryCorp.advice;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.http.HttpStatus;

import yannis.technical.challenge.deliveryCorp.exceptions.OfferNotFoundException;

/**
 * @author YaYa
 *
 */
@ControllerAdvice
public class OfferNotFoundAdvice {

	@ResponseBody
	@ExceptionHandler(OfferNotFoundException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	String offerNotFoundHandler(OfferNotFoundException ex) {
		return ex.getMessage();
	}
}
