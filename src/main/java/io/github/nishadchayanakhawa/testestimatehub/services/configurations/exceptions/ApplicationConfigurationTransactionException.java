package io.github.nishadchayanakhawa.testestimatehub.services.configurations.exceptions;

//spring libraries
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * <b>Class Name</b>: ApplicationConfigurationTransactionException<br>
 * <b>Description</b>: Thrown in case of constraint violation while persisting application
 * configuration. Associated with status code 400-BAD REQUEST<br>
 * @author nishad.chayanakhawa
 */

@ResponseStatus(value = HttpStatus.BAD_REQUEST,reason="Application configuration record is invalid.")
public class ApplicationConfigurationTransactionException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	public ApplicationConfigurationTransactionException(String message) {
		super(message);
	}
}
