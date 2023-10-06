package io.github.nishadchayanakhawa.testestimatehub.services.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.CONFLICT, reason = "Entity with unique values already exists.")
public class DuplicateEntityException extends TestEstimateHubExceptions {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public DuplicateEntityException(String entity, String field, String value) {
		super(String.format("%s '%s' already exists for another %s", field, value,entity));
		status=HttpStatus.CONFLICT;
		reason="Entity with unique values already exists";
	}
}
