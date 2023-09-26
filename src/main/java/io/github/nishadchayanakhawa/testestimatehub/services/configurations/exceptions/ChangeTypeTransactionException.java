package io.github.nishadchayanakhawa.testestimatehub.services.configurations.exceptions;

//spring libraries
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * <b>Class Name</b>: ChangeTypeTransactionException<br>
 * <b>Description</b>: Thrown in case of constraint violation while persisting change type
 * records. Associated with status code 400-BAD REQUEST<br>
 * @author nishad.chayanakhawa
 */

@ResponseStatus(value = HttpStatus.BAD_REQUEST,reason="Change type record is invalid.")
public class ChangeTypeTransactionException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	public ChangeTypeTransactionException(String message) {
		super(message);
	}
}
