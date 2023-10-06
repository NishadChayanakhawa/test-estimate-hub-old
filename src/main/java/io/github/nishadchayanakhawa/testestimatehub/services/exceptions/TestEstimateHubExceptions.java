package io.github.nishadchayanakhawa.testestimatehub.services.exceptions;

import org.springframework.http.HttpStatus;

public class TestEstimateHubExceptions extends RuntimeException {
	protected static HttpStatus status;
	protected static String reason;
	
	private static final long serialVersionUID = 1L;
	
	public TestEstimateHubExceptions(String message) {
		super(message);
	}
	
	public int getStatus() {
		return TestEstimateHubExceptions.status.value();
	}
	
	public String getError() {
		return TestEstimateHubExceptions.status.getReasonPhrase();
	}
	
	public String getReason() {
		return TestEstimateHubExceptions.reason;
	}
	
	public HttpStatus getHttpStatus() {
		return TestEstimateHubExceptions.status;
	}
}
