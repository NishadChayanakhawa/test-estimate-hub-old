package io.github.nishadchayanakhawa.testestimatehub.unittests.modeltests;

import io.github.nishadchayanakhawa.testestimatehub.TestEstimateHubApplication;
import io.github.nishadchayanakhawa.testestimatehub.services.configurations.ApplicationConfigurationService;
import io.github.nishadchayanakhawa.testestimatehub.services.configurations.exceptions.ApplicationConfigurationTransactionException;
import io.github.nishadchayanakhawa.testestimatehub.services.configurations.exceptions.DuplicateApplicationConfigurationException;
import io.github.nishadchayanakhawa.testestimatehub.services.configurations.exceptions.EntityNotFoundException;
import io.nishadc.automationtestingframework.testngcustomization.TestFactory;
import io.github.nishadchayanakhawa.testestimatehub.model.dto.configurations.ApplicationConfigurationDTO;
import java.util.List;
import org.assertj.core.api.Assertions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Test;

@SpringBootTest(classes = TestEstimateHubApplication.class, webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class ApplicationConfigurationServiceTests extends AbstractTestNGSpringContextTests {
	private static Long savedId;

	@Autowired
	private ApplicationConfigurationService applicationConfigurationService;

	@Test
	public void addApplicationConfigurationTest() {
		TestFactory.recordTest("Application Configuration: Add");
		ApplicationConfigurationDTO applicationConfigurationDTO = new ApplicationConfigurationDTO("Test Estimate Hub",
				"User Management", "N/A", 6, "LOW");
		TestFactory
				.recordTestStep(String.format("Persisting application configuration: %s", applicationConfigurationDTO));
		ApplicationConfigurationDTO savedApplicationConfigurationDTO = applicationConfigurationService
				.save(applicationConfigurationDTO);
		TestFactory.recordTestStep(
				String.format("Persisted application configuration: %s", savedApplicationConfigurationDTO));
		ApplicationConfigurationServiceTests.savedId = savedApplicationConfigurationDTO.getId();
		applicationConfigurationDTO.setId(ApplicationConfigurationServiceTests.savedId);
		Assertions.assertThat(savedApplicationConfigurationDTO)
				.hasToString(savedApplicationConfigurationDTO.toString());
	}

	@Test(dependsOnMethods = { "addApplicationConfigurationTest" })
	public void duplicateApplicationConfigurationTest() {
		TestFactory.recordTest("Application Configuration: Add duplicate");
		ApplicationConfigurationDTO applicationConfigurationDTO = new ApplicationConfigurationDTO("Test Estimate Hub",
				"User Management", "N/A", 6, "LOW");
		TestFactory
				.recordTestStep(String.format("Persisting application configuration: %s", applicationConfigurationDTO));
		Assertions.assertThatThrownBy(() -> {
			applicationConfigurationService.save(applicationConfigurationDTO);
		}).isInstanceOf(DuplicateApplicationConfigurationException.class)
				.hasMessage("Application configuration for 'Test Estimate Hub-User Management-N/A' already exists.");
	}

	@Test
	public void invalidApplicationConfigurationTest_zeroTestScripts() {
		TestFactory.recordTest("Application Configuration: With 0 base scripts");
		ApplicationConfigurationDTO applicationConfigurationDTO = new ApplicationConfigurationDTO("Test Estimate Hub",
				"Security", "N/A", 0, "LOW");
		TestFactory
				.recordTestStep(String.format("Persisting application configuration: %s", applicationConfigurationDTO));
		Assertions.assertThatThrownBy(() -> {
			applicationConfigurationService.save(applicationConfigurationDTO);
		}).isInstanceOf(ApplicationConfigurationTransactionException.class)
				.hasMessage("Base test script count cannot be lower than 1.");
	}

	@Test(dependsOnMethods = { "addApplicationConfigurationTest" })
	public void getAllApplicationConfigurationsTest() {
		TestFactory.recordTest("Application Configuration: Get All");
		List<ApplicationConfigurationDTO> applicationConfigurations = this.applicationConfigurationService.getAll();
		TestFactory.recordTestStep(String.format("Application Configuration records: %s", applicationConfigurations));
		Assertions.assertThat(applicationConfigurations).hasSizeGreaterThan(1);
	}

	@Test(dependsOnMethods = { "addApplicationConfigurationTest" })
	public void getApplicationConfigurationsTest() {
		TestFactory.recordTest("Application Configuration: Get By id");
		TestFactory.recordTestStep(String.format("Application Configuration record id: %d", savedId));
		ApplicationConfigurationDTO applicationConfiguration = this.applicationConfigurationService.get(savedId);
		TestFactory.recordTestStep(String.format("Application Configuration record: %s", applicationConfiguration));
		Assertions.assertThat(applicationConfiguration.getId()).isEqualTo(savedId);
	}

	@Test(dependsOnMethods = { "getApplicationConfigurationsTest", "getAllApplicationConfigurationsTest" })
	public void deleteApplicationConfigurationsTest() {
		TestFactory.recordTest("Application Configuration: Delete");
		TestFactory.recordTestStep(String.format("Application Configuration record id to delete: %d", savedId));
		Assertions.assertThat(this.applicationConfigurationService.exists(savedId)).isTrue();
		this.applicationConfigurationService.delete(new ApplicationConfigurationDTO(savedId));
		Assertions.assertThat(this.applicationConfigurationService.exists(savedId)).isFalse();
		Assertions.assertThatThrownBy(() -> this.applicationConfigurationService.get(savedId))
				.isInstanceOf(EntityNotFoundException.class)
				.hasMessage(String.format("Application configuration entity for id %d doesn't exist.", savedId));
	}
}
