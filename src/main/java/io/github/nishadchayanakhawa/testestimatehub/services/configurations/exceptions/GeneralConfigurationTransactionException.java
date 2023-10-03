package io.github.nishadchayanakhawa.testestimatehub.services.configurations.exceptions;

//spring libraries
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * <b>Class Name</b>: GeneralConfigurationTransactionException<br>
 * <b>Description</b>: Thrown in case of constraint violation while persisting change type
 * records. Associated with status code 400-BAD REQUEST<br>
 * @author nishad.chayanakhawa
 */

@ResponseStatus(value = HttpStatus.BAD_REQUEST,reason="General Configuration record is invalid.")
public class GeneralConfigurationTransactionException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	public GeneralConfigurationTransactionException(String message) {
		super(message);
	}
}
