package io.github.nishadchayanakhawa.testestimatehub.integrationtests.api;

import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.springframework.web.context.WebApplicationContext;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import io.github.nishadchayanakhawa.testestimatehub.TestEstimateHubApplication;
import io.github.nishadchayanakhawa.testestimatehub.integrationtests.api.SpringMockRestApiTestHelper.RequestMethod;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import io.restassured.module.mockmvc.response.ValidatableMockMvcResponse;
import io.restassured.module.mockmvc.specification.MockMvcRequestSpecification;

@SpringBootTest(classes = TestEstimateHubApplication.class, webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class ApplicationConfigurationApiTests extends AbstractTestNGSpringContextTests {
	@Value("${server.port}")
	private int port;

	@Autowired
	private WebApplicationContext context;

	private static String url;

	@BeforeClass
	public void setupTestClass() {
		url = String.format("http://localhost:%d/api/%s", port, "configuration/application/1");
		System.out.println(url);
		RestAssuredMockMvc.webAppContextSetup(context);
	}

	@Test
	@WithMockUser(roles = "ADMIN")
	public void getApplicationConfigurationTest() throws JSONException {
		MockMvcRequestSpecification request=SpringMockRestApiTestHelper.formRequest(null, null, null);
		ValidatableMockMvcResponse response=SpringMockRestApiTestHelper.getRespones(request, url, RequestMethod.GET);
		SpringMockRestApiTestHelper.validateResponse(response, 200, null);
	}
}
