package io.github.nishadchayanakhawa.testestimatehub.services.configurations.exceptions;

//spring libraries
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * <b>Class Name</b>: DuplicateChangeTypeException<br>
 * <b>Description</b>: Thrown when duplicate entry is persisted.
 * Associated with Http Status 409-CONFLICT<br>
 * @author nishad.chayanakhawa
 */
@ResponseStatus(value = HttpStatus.CONFLICT,reason="Application configuration record already exists.")
public class DuplicateChangeTypeException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	public DuplicateChangeTypeException(String message) {
		super(message);
	}
}
