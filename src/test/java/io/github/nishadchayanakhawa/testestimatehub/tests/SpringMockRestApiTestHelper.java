package io.github.nishadchayanakhawa.testestimatehub.tests;

import java.util.Map;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import org.hamcrest.Matchers;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import io.nishadc.automationtestingframework.testngcustomization.TestFactory;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import io.restassured.module.mockmvc.response.ValidatableMockMvcResponse;
import io.restassured.module.mockmvc.specification.MockMvcRequestSpecification;
public class SpringMockRestApiTestHelper {
	private static Logger logger = LoggerFactory.getLogger(SpringMockRestApiTestHelper.class);
	public enum RequestMethod {
		GET, POST, PUT, DELETE, PATCH, OPTIONS
	}
	
	public static String[] toArrayOfStrings(JSONArray jsonArray) throws JSONException {
		List<String> stringList=new ArrayList<>();
		for(int iJsonCounter=0;iJsonCounter<jsonArray.length();iJsonCounter++) {
			stringList.add(jsonArray.getString(iJsonCounter));
		}
		return stringList.toArray(new String[stringList.size()]);
	}
	
	public static Map<String,Object> toMap(JSONObject json, String ... fieldsToskip) throws JSONException {
		Map<String,Object> map=new HashMap<>();
		Iterator<?> jsonKeys=json.keys();
		List<String> fieldsToSkipList=Arrays.asList(fieldsToskip);
		while(jsonKeys.hasNext()) {
			String key=(String)jsonKeys.next();
			if(!(fieldsToSkipList.contains(key))) {
				Object value=json.get(key);
				map.put(key, value);
			}
		}
		return map;
	}

	private SpringMockRestApiTestHelper() {
		// Do Nothing
		// Object will never be instantiated and methods are expected to be used through
		// static references
	}

	public static MockMvcRequestSpecification formRequest(Map<String, String> headersMap, Map<String, String> parametersMap,
			JSONObject requestBody) throws JSONException {
		SpringMockRestApiTestHelper.logger.debug("Forming request for REST API call");

		// create Request Specification object
		MockMvcRequestSpecification request = RestAssuredMockMvc.given(); // url encoding is disabled

		// set headers if non-null
		if (headersMap != null) {
			request.headers(headersMap);
			SpringMockRestApiTestHelper.logger.debug("Request Headers: {}", headersMap);
			TestFactory.recordTestStep(
					String.format("Headers: <br><textarea>%s</textarea>", new JSONObject(headersMap).toString(1)));
		} else {
			SpringMockRestApiTestHelper.logger.debug("Request Headers passed are null and hence won't be attached");
			TestFactory.recordTestStep("No Headers attached");
		}

		// set parameters if non-null
		if (parametersMap != null) {
			request.params(parametersMap);
			SpringMockRestApiTestHelper.logger.debug("Request Parameters: {}", parametersMap);
			TestFactory.recordTestStep(String.format("Parameters: <br><textarea>%s</textarea>",
					new JSONObject(parametersMap).toString(1)));
		} else {
			SpringMockRestApiTestHelper.logger.debug("Request Parameters passed are null and hence won't be attached");
			TestFactory.recordTestStep("No Parameters attached");
		}

		// set request body if non-null
		if (requestBody != null) {
			request.body(requestBody.toString());
			SpringMockRestApiTestHelper.logger.debug("Request Body: {}", requestBody);
			TestFactory.recordTestStep(
					String.format("Request Body: <br><textarea>%s</textarea>", requestBody.toString(1)));
		} else {
			SpringMockRestApiTestHelper.logger.debug("Request Body passed is null and hence won't be attached");
			TestFactory.recordTestStep("No Request Body attached");
		}

		// return request specification created
		return request;
	}
	
	public static ValidatableMockMvcResponse getRespones(MockMvcRequestSpecification request,String url,RequestMethod requestMethod) {
		SpringMockRestApiTestHelper.logger.debug("Submitting response as {} to {}",requestMethod,url);
		ValidatableMockMvcResponse response=SpringMockRestApiTestHelper.submitRequest(request, url, requestMethod);
		
		//log responses
		SpringMockRestApiTestHelper.logger.debug("Response Status code: {}",response.extract().statusCode());
		SpringMockRestApiTestHelper.logger.debug("Response Headers: {}",response.extract().headers());
		SpringMockRestApiTestHelper.logger.debug("Response Body: {}",response.extract().body().asPrettyString());
		
		//Add test steps
		TestFactory.recordTestStep(String.format("Response status code: %d", response.extract().statusCode()));
		TestFactory.recordTestStep(String.format("Response headers: <br><textarea>%s</textarea>", response.extract().headers()));
		TestFactory.recordTestStep(String.format("Response body: <br><textarea>%s</textarea>", response.extract().body().asPrettyString()));
		
		//return response
		return response;
	}
	
	private static ValidatableMockMvcResponse submitRequest(MockMvcRequestSpecification request,String url,RequestMethod requestMethod) {
		TestFactory.recordTestStep(String.format("Submitting '%s' request to '%s'", requestMethod.toString(),url));
		//call restassured method based on request method type
		switch(requestMethod) {
		case DELETE:
			return request.delete(url).then();
		case GET:
			return request.get(url).then();
		case OPTIONS:
			return request.options(url).then();
		case PATCH:
			return request.patch(url).then();
		case POST:
			return request.post(url).then();
		case PUT:
			return request.put(url).then();
		default:
			return null;
		}
	}
	
	public static Object query(ValidatableMockMvcResponse response,String jsonPath) {
		return response
				.extract()
				.path(jsonPath);
	}
	
	public static void validateResponse(ValidatableMockMvcResponse response,int expectedStatusCode,Map<String,Object> bodyValidationMap) {
		SpringMockRestApiTestHelper.logger.debug("Validating response");
		
		//validate response status code
		if(expectedStatusCode>0) {
			response
				.assertThat()
					.statusCode(expectedStatusCode);
		} else {
			SpringMockRestApiTestHelper.logger.debug("Expected status code is 0, response code validation skipped.");
		}
		
		//validate response body
		if(bodyValidationMap!=null) {
			for(Entry<String,Object> bodyValidationEntry : bodyValidationMap.entrySet()) {
				String jsonPath=bodyValidationEntry.getKey();
				Object expectedValue=bodyValidationEntry.getValue();
				
				if("*".equals(expectedValue)) {
					//check for non null when expected value is *
					SpringMockRestApiTestHelper.logger.debug("Validating if jsonpath {} has non-null value",jsonPath);
					response
						.assertThat()
							.body(jsonPath, Matchers.notNullValue());
				} else if(expectedValue.toString().contains("%%")) {
					//check for wildcard string
					String expectedWildCard=expectedValue.toString().replace("%%", "");
					response
					.assertThat()
						.body(jsonPath, Matchers.containsString(expectedWildCard));
				} else {
					//check for actual object value
					SpringMockRestApiTestHelper.logger.debug("Validating if jsonpath {} has value {}",jsonPath,expectedValue);
					response
					.assertThat()
						.body(jsonPath, Matchers.equalToObject(expectedValue));
				}
			}
		}
	}
}
