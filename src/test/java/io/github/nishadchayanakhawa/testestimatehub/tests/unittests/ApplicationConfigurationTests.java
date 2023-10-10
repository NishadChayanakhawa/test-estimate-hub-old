package io.github.nishadchayanakhawa.testestimatehub.tests.unittests;

import io.github.nishadchayanakhawa.testestimatehub.TestEstimateHubApplication;
import io.github.nishadchayanakhawa.testestimatehub.model.dto.ApplicationConfigurationDTO;
import io.github.nishadchayanakhawa.testestimatehub.services.ApplicationConfigurationService;
import io.github.nishadchayanakhawa.testestimatehub.services.exceptions.DuplicateEntityException;
import io.github.nishadchayanakhawa.testestimatehub.services.exceptions.EntityNotFoundException;
import io.github.nishadchayanakhawa.testestimatehub.services.exceptions.TransactionException;
import io.github.nishadchayanakhawa.testestimatehub.tests.SpringMockRestApiTestHelper;
import io.github.nishadchayanakhawa.testestimatehub.tests.TestDataProvider;
import io.nishadc.automationtestingframework.testngcustomization.TestFactory;
import org.assertj.core.api.Assertions;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest(classes = TestEstimateHubApplication.class, webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class ApplicationConfigurationTests extends AbstractTestNGSpringContextTests {

	@Autowired
	private ApplicationConfigurationService applicationConfigurationService;

	private static ObjectMapper objectMapper = new ObjectMapper();

	private ApplicationConfigurationDTO save(ApplicationConfigurationDTO applicationConfiguration) {
		TestFactory.recordTestStep(String.format("Saving Application configuration: %s", applicationConfiguration));
		ApplicationConfigurationDTO savedApplicationConfiguration = this.applicationConfigurationService
				.save(applicationConfiguration);
		TestFactory.recordTestStep(String.format("Saved Application configuration: %s", savedApplicationConfiguration));
		return savedApplicationConfiguration;
	}

	@Test(dataProvider = "getTestDataFromJson", dataProviderClass = TestDataProvider.class, groups = { "unit-test" })
	@WithMockUser(roles = "ADMIN")
	public void addApplicationConfiguration_test(JSONObject applicationConfigurationToAddJson)
			throws JSONException, JsonMappingException, JsonProcessingException {
		TestFactory.recordTest("Add/Get/GetAll/Delete Application Configuration");
		// add entity
		ApplicationConfigurationDTO applicationConfigurationToAdd = objectMapper
				.readValue(applicationConfigurationToAddJson.toString(), ApplicationConfigurationDTO.class);
		ApplicationConfigurationDTO applicationConfigurationAdded = this.save(applicationConfigurationToAdd);

		Assertions.assertThat(applicationConfigurationAdded.getId()).isPositive();
		Assertions.assertThat(applicationConfigurationAdded).usingRecursiveComparison()
				.ignoringFields("id", "complexityDisplayValue").isEqualTo(applicationConfigurationToAdd);

		// Get if of added entity
		Long id = applicationConfigurationAdded.getId();

		// get entity
		ApplicationConfigurationDTO retrievedEntity = this.applicationConfigurationService.get(id);
		Assertions.assertThat(retrievedEntity).usingRecursiveComparison().isEqualTo(applicationConfigurationAdded);

		// get all entities
		Assertions.assertThat(this.applicationConfigurationService.getAll()).isNotEmpty();

		// delete entity
		this.applicationConfigurationService.delete(id);

		// get deleted entity
		Assertions.assertThatThrownBy(() -> this.applicationConfigurationService.get(id))
				.isExactlyInstanceOf(EntityNotFoundException.class)
				.hasMessage(String.format("%s with id %d doesn't exist.", "Application Configuration", id));
	}

	@Test(dataProvider = "getTestDataFromJson", dataProviderClass = TestDataProvider.class, groups = { "unit-test" })
	@WithMockUser(roles = "ADMIN")
	public void invalidRecord_test(JSONObject recordDetails)
			throws JSONException, JsonMappingException, JsonProcessingException {
		TestFactory.recordTest("Add Application Configuration with invalid requests");
		// add entity
		JSONObject baseConfigurationToAdd = recordDetails.getJSONObject("applicationConfigurationToAdd");
		ApplicationConfigurationDTO originalRecordToAdd = objectMapper.readValue(baseConfigurationToAdd.toString(),
				ApplicationConfigurationDTO.class);
		ApplicationConfigurationDTO originalRecordAdded = this.save(originalRecordToAdd);

		// get id of added entity
		Long id = originalRecordAdded.getId();

		// add duplicate record
		JSONObject duplicateConfigurationToAdd = recordDetails.getJSONObject("duplicateApplicationConfigurationToAdd");
		String[] expectedErrorDetails = SpringMockRestApiTestHelper
				.toArrayOfStrings(recordDetails.getJSONArray("expectedErrors_duplicateApplicationConfiguration"));
		ApplicationConfigurationDTO duplicateRecordToAdd = objectMapper
				.readValue(duplicateConfigurationToAdd.toString(), ApplicationConfigurationDTO.class);
		Assertions.assertThatThrownBy(() -> this.save(duplicateRecordToAdd))
				.isExactlyInstanceOf(DuplicateEntityException.class).hasMessageContainingAll(expectedErrorDetails);
		// add invalid record
		JSONObject invalidConfigurationToAdd = recordDetails.getJSONObject("invalidApplicationConfigurationToAdd");
		ApplicationConfigurationDTO invalidRecordToAdd = objectMapper.readValue(invalidConfigurationToAdd.toString(),
				ApplicationConfigurationDTO.class);
		expectedErrorDetails = SpringMockRestApiTestHelper
				.toArrayOfStrings(recordDetails.getJSONArray("expectedErrors_invalidRequest"));

		Throwable transactionException = Assertions
				.catchThrowable(() -> this.applicationConfigurationService.save(invalidRecordToAdd));
		Assertions.assertThat(transactionException).isExactlyInstanceOf(TransactionException.class);

		String[] actualErrorMessage = transactionException.getMessage().split(";");

		Assertions.assertThat(actualErrorMessage).containsExactlyInAnyOrder(expectedErrorDetails);

		this.applicationConfigurationService.delete(id);
	}

}
