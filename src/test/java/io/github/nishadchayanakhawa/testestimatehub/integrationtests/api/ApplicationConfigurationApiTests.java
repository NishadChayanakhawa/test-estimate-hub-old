package io.github.nishadchayanakhawa.testestimatehub.integrationtests.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.springframework.web.context.WebApplicationContext;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import io.github.nishadchayanakhawa.testestimatehub.TestEstimateHubApplication;
import io.restassured.module.mockmvc.RestAssuredMockMvc;

@SpringBootTest(classes = TestEstimateHubApplication.class, webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class ApplicationConfigurationApiTests extends AbstractTestNGSpringContextTests {
	@Value("${server.port}")
	private int port;
	
	@Autowired
    private WebApplicationContext context;
	
	private static String url; 
	
	@BeforeClass
	public void setupTestClass() {
		url=String.format("http://localhost:%d/api/%s", port,"configuration/application/1");
		System.out.println(url);
		RestAssuredMockMvc.webAppContextSetup(context);
	}
	
	@Test
	public void getApplicationConfigurationTest() {
		RestAssuredMockMvc
			.given()
			.get(url)
			.then()
			.assertThat()
				.statusCode(200);
	}
}
