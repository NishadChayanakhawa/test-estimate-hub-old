package io.github.nishadchayanakhawa.testestimatehub.services.exceptions;

import java.util.Set;
import java.util.stream.Collectors;

//spring libraries
import org.springframework.http.HttpStatus;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.web.bind.annotation.ResponseStatus;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.ConstraintViolation;

/**
 * <b>Class Name</b>: EntityNotFoundException<br>
 * <b>Description</b>: Thrown in case of transactional error with entity.
 * Associated with status code 400.<br>
 * 
 * @author nishad.chayanakhawa
 */

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Request data is incorrect.")
public class TransactionException extends TestEstimateHubExceptions {
	private static final long serialVersionUID = 1L;

	public TransactionException(String message) {
		super(message, HttpStatus.BAD_REQUEST, "Request data is incorrect");
	}

	public TransactionException(TransactionSystemException e) {
		this(TransactionException.extractMessages(e));
	}

	private static String extractMessages(TransactionSystemException e) {
		// in case constraints other than unique are violated,
		// get violation messages
		ConstraintViolationException re = (ConstraintViolationException) e.getRootCause();
		if (re == null) {
			return e.getMessage();
		} else {
			Set<ConstraintViolation<?>> violations = re.getConstraintViolations();
			// join individual messages by
			return violations.stream().map(ConstraintViolation::getMessage).collect(Collectors.joining(";"));
		}
	}
}
