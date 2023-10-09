package io.github.nishadchayanakhawa.testestimatehub.tests.integrationtests.api;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

import java.util.Map;

import org.hamcrest.Matchers;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.springframework.web.context.WebApplicationContext;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import io.github.nishadchayanakhawa.testestimatehub.TestEstimateHubApplication;
import io.github.nishadchayanakhawa.testestimatehub.configurations.TestEstimateHubConstants;
import io.github.nishadchayanakhawa.testestimatehub.tests.SpringMockRestApiTestHelper;
import io.github.nishadchayanakhawa.testestimatehub.tests.TestDataProvider;
import io.github.nishadchayanakhawa.testestimatehub.tests.SpringMockRestApiTestHelper.RequestMethod;
import io.nishadc.automationtestingframework.testngcustomization.TestFactory;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import io.restassured.module.mockmvc.response.ValidatableMockMvcResponse;
import io.restassured.module.mockmvc.specification.MockMvcRequestSpecification;

@ExtendWith(SpringExtension.class)
@ContextConfiguration
@SpringBootTest(classes = TestEstimateHubApplication.class, webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class ApplicationConfigurationTests extends AbstractTestNGSpringContextTests {
	@Value("${server.port}")
	private int port;

	@Autowired
	private WebApplicationContext context;

	private static String url;

	@BeforeClass(groups = { "api-test" })
	public void setupTestClass() {
		url = String.format("http://localhost:%d%s", port, TestEstimateHubConstants.APPLICATION_CONFIGURATION_API);
		RestAssuredMockMvc.webAppContextSetup(context);
		RestAssuredMockMvc.postProcessors(csrf().asHeader());
	}

	private ValidatableMockMvcResponse add(JSONObject applicationConfigurationToAdd) throws JSONException {
		TestFactory
				.recordTestStep(String.format("Application Configuration to add: %s", applicationConfigurationToAdd));
		MockMvcRequestSpecification request = SpringMockRestApiTestHelper.formRequest(
				Map.of("Content-Type", MediaType.APPLICATION_JSON_VALUE), null, applicationConfigurationToAdd);
		return SpringMockRestApiTestHelper.getRespones(request, url, RequestMethod.PUT);
	}

	private ValidatableMockMvcResponse delete(Long id) throws JSONException {
		JSONObject entityToDelete = new JSONObject(String.format("{'id' : %d}", id));
		TestFactory.recordTestStep(String.format("Deleting Application Configuration : %s", entityToDelete));
		MockMvcRequestSpecification request = SpringMockRestApiTestHelper
				.formRequest(Map.of("Content-Type", MediaType.APPLICATION_JSON_VALUE), null, entityToDelete);
		return SpringMockRestApiTestHelper.getRespones(request, url, RequestMethod.DELETE);
	}

	@Test(dataProvider = "getTestDataFromJson", dataProviderClass = TestDataProvider.class, groups = { "api-test" })
	@WithMockUser(roles = "ADMIN")
	public void addApplicationConfiguration_test(JSONObject applicationConfigurationToAdd) throws JSONException {
		TestFactory.recordTest("Add/Get/GetAll/Delete Application Configuration");

		// ads application configuration
		ValidatableMockMvcResponse response = this.add(applicationConfigurationToAdd);

		// verify added entity
		Map<String, Object> addedApplicationConfiguration = SpringMockRestApiTestHelper
				.toMap(applicationConfigurationToAdd);
		SpringMockRestApiTestHelper.validateResponse(response, 201, addedApplicationConfiguration);

		// get id of added entity
		Long addedId = Long.parseLong(SpringMockRestApiTestHelper.query(response, "id").toString());

		// get added entity
		MockMvcRequestSpecification request = SpringMockRestApiTestHelper.formRequest(null, null, null);
		response = SpringMockRestApiTestHelper.getRespones(request, String.format("%s/%d", url, addedId),
				RequestMethod.GET);
		SpringMockRestApiTestHelper.validateResponse(response, 200, addedApplicationConfiguration);

		// Get all entities
		response = SpringMockRestApiTestHelper.getRespones(request, url, RequestMethod.GET);
		SpringMockRestApiTestHelper.validateResponse(response, 200, null);

		// delete entity
		this.delete(addedId);

		// get deleted entry
		request = SpringMockRestApiTestHelper.formRequest(null, null, null);
		response = SpringMockRestApiTestHelper.getRespones(request, String.format("%s/%d", url, addedId),
				RequestMethod.GET);
		SpringMockRestApiTestHelper.validateResponse(response, 410, null);
	}

	@Test(dataProvider = "getTestDataFromJson", dataProviderClass = TestDataProvider.class, groups = { "api-test" })
	@WithMockUser(roles = "ADMIN")
	public void invalidRecord_test(JSONObject recordDetails) throws JSONException {
		TestFactory.recordTest("Add Application Configuration with invalid requests");
		// add entity
		JSONObject baseConfigurationToAdd = recordDetails.getJSONObject("applicationConfigurationToAdd");
		ValidatableMockMvcResponse response = this.add(baseConfigurationToAdd);
		// get id of added entity
		Long addedId = Long.parseLong(SpringMockRestApiTestHelper.query(response, "id").toString());

		// add duplicate record
		JSONObject duplicateConfigurationToAdd = recordDetails.getJSONObject("duplicateApplicationConfigurationToAdd");
		response = this.add(duplicateConfigurationToAdd);

		JSONArray duplicateError = recordDetails.getJSONArray("expectedErrors_duplicateApplicationConfiguration");
		String[] expectedErrorDetails = SpringMockRestApiTestHelper.toArrayOfStrings(duplicateError);
		SpringMockRestApiTestHelper.validateResponse(response, 409, null);
		response.assertThat().body("details", Matchers.containsInAnyOrder(expectedErrorDetails));

		// add invalid record
		JSONObject invalidApplicationConfigurationToAdd = recordDetails
				.getJSONObject("invalidApplicationConfigurationToAdd");
		JSONArray invalidBodyError = recordDetails.getJSONArray("expectedErrors_invalidRequest");
		expectedErrorDetails = SpringMockRestApiTestHelper.toArrayOfStrings(invalidBodyError);
		response = this.add(invalidApplicationConfigurationToAdd);
		SpringMockRestApiTestHelper.validateResponse(response, 400, null);
		response.assertThat().body("details", Matchers.containsInAnyOrder(expectedErrorDetails));

		this.delete(addedId);
	}
	
	@Test(dataProvider = "getTestDataFromJson", dataProviderClass = TestDataProvider.class, groups = { "api-test" })
	@WithMockUser(roles = "ADMIN")
	public void modifyRecord_test(JSONObject recordDetails) throws JSONException {
		TestFactory.recordTest("Modify Application Configuration");
		// add entity
		JSONObject originalConfigurationToAdd = recordDetails.getJSONObject("originalApplicationConfigurationToAdd");
		ValidatableMockMvcResponse response = this.add(originalConfigurationToAdd);
		// get id of added entity
		Long addedId = Long.parseLong(SpringMockRestApiTestHelper.query(response, "id").toString());

		// modify record
		JSONObject configurationToModify = recordDetails.getJSONObject("modifiedApplicationConfigurationToAdd");
		configurationToModify.put("id", addedId);
		response = this.add(configurationToModify);
		
		Map<String, Object> modifiedApplicationConfiguration = SpringMockRestApiTestHelper
				.toMap(configurationToModify,"id");

		SpringMockRestApiTestHelper.validateResponse(response, 200, modifiedApplicationConfiguration);

		this.delete(addedId);
	}
}
