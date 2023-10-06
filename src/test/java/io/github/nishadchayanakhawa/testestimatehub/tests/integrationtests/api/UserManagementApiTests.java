package io.github.nishadchayanakhawa.testestimatehub.tests.integrationtests.api;

import java.util.Map;
import org.json.JSONObject;
import org.json.JSONArray;
import org.junit.jupiter.api.extension.ExtendWith;
import org.hamcrest.Matchers;
import org.json.JSONException;
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

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

import io.github.nishadchayanakhawa.testestimatehub.TestEstimateHubApplication;
import io.github.nishadchayanakhawa.testestimatehub.configurations.TestEstimateHubConstants;
import io.github.nishadchayanakhawa.testestimatehub.tests.SpringMockRestApiTestHelper;
import io.github.nishadchayanakhawa.testestimatehub.tests.SpringMockRestApiTestHelper.RequestMethod;
import io.nishadc.automationtestingframework.testngcustomization.TestFactory;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import io.restassured.module.mockmvc.response.ValidatableMockMvcResponse;
import io.restassured.module.mockmvc.specification.MockMvcRequestSpecification;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

@ExtendWith(SpringExtension.class)
@ContextConfiguration
@SpringBootTest(classes = TestEstimateHubApplication.class, webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class UserManagementApiTests extends AbstractTestNGSpringContextTests {
	@Value("${server.port}")
	private int port;

	@Autowired
	private WebApplicationContext context;

	private static String url;

	@BeforeClass(groups = { "api-test" })
	public void setupTestClass() {
		url = String.format("http://localhost:%d%s", port, TestEstimateHubConstants.USER_MANAGEMENT_API);
		RestAssuredMockMvc.webAppContextSetup(context);
		RestAssuredMockMvc.postProcessors(csrf().asHeader());
	}

	private ValidatableMockMvcResponse addUser(JSONObject user) throws JSONException {
		TestFactory.recordTestStep(String.format("User to add/modify: %s", user.toString()));
		MockMvcRequestSpecification request = SpringMockRestApiTestHelper
				.formRequest(Map.of("Content-Type", MediaType.APPLICATION_JSON_VALUE), null, user);
		return SpringMockRestApiTestHelper.getRespones(request, url, RequestMethod.PUT);
	}

	private void deleteUser(Long id) throws JSONException {
		JSONObject userToDelete = new JSONObject(String.format("{'id' : %d}", id));
		MockMvcRequestSpecification request = SpringMockRestApiTestHelper
				.formRequest(Map.of("Content-Type", MediaType.APPLICATION_JSON_VALUE), null, userToDelete);
		ValidatableMockMvcResponse response = SpringMockRestApiTestHelper.getRespones(request, url,
				RequestMethod.DELETE);
		SpringMockRestApiTestHelper.validateResponse(response, 200, null);
	}

	@Test(dataProvider = "getTestDataFromJson", dataProviderClass = ApiTestDataProvider.class, groups = { "api-test" })
	@WithMockUser(roles = "ADMIN")
	public void addUser_test(JSONObject addUser) throws JSONException, JsonMappingException, JsonProcessingException {
		TestFactory.recordTest("Add/Get/GetAll/Delete User");
		// add user
		ValidatableMockMvcResponse response = this.addUser(addUser);
		Long addedUserId = Long.parseLong(SpringMockRestApiTestHelper.query(response, "id").toString());

		// get added user map
		Map<String, Object> addUserMap = SpringMockRestApiTestHelper.toMap(addUser, "roles", "password");
		// validate added user
		SpringMockRestApiTestHelper.validateResponse(response, 201, addUserMap);
		
		//get user
		MockMvcRequestSpecification request = SpringMockRestApiTestHelper
				.formRequest(null, null, null);
		response = SpringMockRestApiTestHelper.getRespones(request, String.format("%s/%d", url,addedUserId), RequestMethod.GET);
		SpringMockRestApiTestHelper.validateResponse(response, 200, addUserMap);

		//get all users
		request = SpringMockRestApiTestHelper
				.formRequest(null, null, null);
		response = SpringMockRestApiTestHelper.getRespones(request, url, RequestMethod.GET);
		SpringMockRestApiTestHelper.validateResponse(response, 200, null);
		
		//delete user
		this.deleteUser(addedUserId);
		
		//get deleted user
		request = SpringMockRestApiTestHelper
				.formRequest(null, null, null);
		response = SpringMockRestApiTestHelper.getRespones(request, String.format("%s/%d", url,addedUserId), RequestMethod.GET);
		SpringMockRestApiTestHelper.validateResponse(response, 410,null);
	}

	@Test(dataProvider = "getTestDataFromJson", dataProviderClass = ApiTestDataProvider.class, groups = { "api-test" })
	@WithMockUser(roles = "ADMIN")
	public void modifyUser_test(JSONObject addUser)
			throws JSONException, JsonMappingException, JsonProcessingException {
		TestFactory.recordTest("Modify User");
		// add user for modification
		JSONObject userToAdd = addUser.getJSONObject("userToAdd");
		ValidatableMockMvcResponse response = this.addUser(userToAdd);
		// get id of added user
		Long addedUserId = Long.parseLong(SpringMockRestApiTestHelper.query(response, "id").toString());

		// modify user
		JSONObject userToModify = addUser.getJSONObject("userToModify");
		userToModify.put("id", addedUserId);

		Map<String, Object> modifiedUserMap = SpringMockRestApiTestHelper.toMap(userToModify, "roles", "password",
				"id");

		response = this.addUser(userToModify);
		SpringMockRestApiTestHelper.validateResponse(response, 200, modifiedUserMap);

		// modify user without password change
		userToModify.remove("password");
		response = this.addUser(userToModify);
		SpringMockRestApiTestHelper.validateResponse(response, 200, modifiedUserMap);

		this.deleteUser(addedUserId);
	}

	@Test(dataProvider = "getTestDataFromJson", dataProviderClass = ApiTestDataProvider.class, groups = { "api-test" })
	@WithMockUser(roles = "ADMIN")
	public void addUser_invalidRequest_test(JSONObject addUser)
			throws JSONException, JsonMappingException, JsonProcessingException {
		TestFactory.recordTest("Add User with invalid requests");
		// add user
		JSONObject userToAdd = addUser.getJSONObject("userToAdd");
		ValidatableMockMvcResponse response = this.addUser(userToAdd);
		// get id of added user
		Long addedUserId = Long.parseLong(SpringMockRestApiTestHelper.query(response, "id").toString());
		// add user with duplicate username
		JSONObject duplicateUsernameToAdd = addUser.getJSONObject("duplicateUsernameToAdd");
		response = this.addUser(duplicateUsernameToAdd);
		// validate error response
		JSONArray duplicateUsernameError = addUser.getJSONArray("expectedErrors_duplicateUsername");
		String[] errorDetails = SpringMockRestApiTestHelper.toArrayOfStrings(duplicateUsernameError);
		SpringMockRestApiTestHelper.validateResponse(response, 409, null);
		response.assertThat().body("details", Matchers.containsInAnyOrder(errorDetails));

		// add user with duplicate email
		JSONObject duplicateEmailToAdd = addUser.getJSONObject("duplicateEmailToAdd");
		response = this.addUser(duplicateEmailToAdd);
		// validate error response
		JSONArray duplicateEmailError = addUser.getJSONArray("expectedErrors_duplicateEmail");
		errorDetails = SpringMockRestApiTestHelper.toArrayOfStrings(duplicateEmailError);
		SpringMockRestApiTestHelper.validateResponse(response, 409, null);
		response.assertThat().body("details", Matchers.containsInAnyOrder(errorDetails));

		// add user with invalid request
		JSONObject invalidUserRequestToAdd = addUser.getJSONObject("invalidUserToAdd");
		response = this.addUser(invalidUserRequestToAdd);
		// validate error response
		JSONArray invalidRequestError = addUser.getJSONArray("expectedErrors_invalidRequest");
		errorDetails = SpringMockRestApiTestHelper.toArrayOfStrings(invalidRequestError);
		SpringMockRestApiTestHelper.validateResponse(response, 400, null);
		response.assertThat().body("details", Matchers.containsInAnyOrder(errorDetails));

		//delete user
		this.deleteUser(addedUserId);
	}
}
