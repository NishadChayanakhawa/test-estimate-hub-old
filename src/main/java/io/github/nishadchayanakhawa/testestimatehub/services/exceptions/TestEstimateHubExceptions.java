package io.github.nishadchayanakhawa.testestimatehub.services.exceptions;

import org.springframework.http.HttpStatus;

public class TestEstimateHubExceptions extends RuntimeException {
	private static HttpStatus status;
	private static String reason;
	
	private static final long serialVersionUID = 1L;
	
	public TestEstimateHubExceptions(String message) {
		super(message);
	}
	
	public TestEstimateHubExceptions(String message,HttpStatus status,String reason) {
		this(message);
		this.status=status;
		this.reason=reason;
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
