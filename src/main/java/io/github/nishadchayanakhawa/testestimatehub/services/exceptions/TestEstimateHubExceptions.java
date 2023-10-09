package io.github.nishadchayanakhawa.testestimatehub.services.exceptions;

import org.springframework.http.HttpStatus;

public class TestEstimateHubExceptions extends RuntimeException {
	private HttpStatus status;
	private String reason;
	
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
		return this.status.value();
	}
	
	public String getError() {
		return this.status.getReasonPhrase();
	}
	
	public String getReason() {
		return this.reason;
	}
	
	public HttpStatus getHttpStatus() {
		return this.status;
	}
}
