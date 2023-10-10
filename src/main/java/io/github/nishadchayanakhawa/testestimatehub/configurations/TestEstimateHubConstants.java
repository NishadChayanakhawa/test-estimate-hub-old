package io.github.nishadchayanakhawa.testestimatehub.configurations;

public class TestEstimateHubConstants {
	private TestEstimateHubConstants() {
		
	}
	private static final String CONFIGURATION_API="/api/configuration";
	
	public static final String USER_MANAGEMENT_API=CONFIGURATION_API + "/user";
	public static final String APPLICATION_CONFIGURATION_API= CONFIGURATION_API + "/application";
	
	public static final String SERVING_REQUEST_DEBUG_MESSAGE="Serving {} request for {}";
}
