package io.github.nishadchayanakhawa.testestimatehub.tests;

public class Constants {
	private static final String REQUIRED_FIELD_ERROR_MESSAGE="cannot be blank";
	
	public static String getRequiredFieldErrorMessage(String fieldName) {
		return String.format("%s %s", fieldName,Constants.REQUIRED_FIELD_ERROR_MESSAGE);
	}
}
