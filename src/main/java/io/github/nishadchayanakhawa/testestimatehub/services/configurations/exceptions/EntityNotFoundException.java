package io.github.nishadchayanakhawa.testestimatehub.services.configurations.exceptions;

//spring libraries
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * <b>Class Name</b>: EntityNotFoundException<br>
 * <b>Description</b>: Thrown in case entity is not found in db. Associated with status code 404<br>
 * @author nishad.chayanakhawa
 */

@ResponseStatus(value = HttpStatus.NOT_FOUND,reason="Requested entity doesn't exist.")
public class EntityNotFoundException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	public EntityNotFoundException(String message) {
		super(message);
	}
}
