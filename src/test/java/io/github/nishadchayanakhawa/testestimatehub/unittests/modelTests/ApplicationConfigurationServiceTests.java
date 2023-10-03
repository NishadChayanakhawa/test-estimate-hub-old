package io.github.nishadchayanakhawa.testestimatehub.unittests.modelTests;

import io.github.nishadchayanakhawa.testestimatehub.TestEstimateHubApplication;
import io.github.nishadchayanakhawa.testestimatehub.services.configurations.ApplicationConfigurationService;
import io.github.nishadchayanakhawa.testestimatehub.services.configurations.exceptions.ApplicationConfigurationTransactionException;
import io.github.nishadchayanakhawa.testestimatehub.services.configurations.exceptions.DuplicateApplicationConfigurationException;
import io.nishadc.automationtestingframework.testngcustomization.TestFactory;
import io.github.nishadchayanakhawa.testestimatehub.model.dto.configurations.ApplicationConfigurationDTO;
import org.assertj.core.api.Assertions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Test;
@SpringBootTest(classes = TestEstimateHubApplication.class, webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class ApplicationConfigurationServiceTests extends AbstractTestNGSpringContextTests {

	@Autowired
	private ApplicationConfigurationService applicationConfigurationService;

	@Test
	public void addApplicationConfigurationTest() {
		TestFactory.recordTest("Add application configuration test");
		ApplicationConfigurationDTO applicationConfigurationDTO = new ApplicationConfigurationDTO(
				"Test Estimate Hub", "User Management", "N/A", 6, "LOW");
		TestFactory.recordTestStep(String.format("Persisting application configuration: %s", applicationConfigurationDTO));
		ApplicationConfigurationDTO savedApplicationConfigurationDTO = applicationConfigurationService
				.save(applicationConfigurationDTO);
		TestFactory.recordTestStep(String.format("Persisted application configuration: %s", savedApplicationConfigurationDTO));
		applicationConfigurationDTO.setId(savedApplicationConfigurationDTO.getId());
		Assertions.assertThat(savedApplicationConfigurationDTO).hasToString(savedApplicationConfigurationDTO.toString());
	}

	@Test(dependsOnMethods= {"addApplicationConfigurationTest"})
	public void duplicateApplicationConfigurationTest() {
		TestFactory.recordTest("Add duplicate application configuration test");
		ApplicationConfigurationDTO applicationConfigurationDTO = new ApplicationConfigurationDTO(
				"Test Estimate Hub", "User Management", "N/A", 6, "LOW");
		TestFactory.recordTestStep(String.format("Persisting application configuration: %s", applicationConfigurationDTO));
		Assertions.assertThatThrownBy(() -> {
			applicationConfigurationService.save(applicationConfigurationDTO);
		}).isInstanceOf(DuplicateApplicationConfigurationException.class)
				.hasMessage("Application configuration for 'Test Estimate Hub-User Management-N/A' already exists.");
	}
	
	@Test
	public void invalidApplicationConfigurationTest_zeroTestScripts() {
		TestFactory.recordTest("Add application configuration test with 0 base test scripts.");
		ApplicationConfigurationDTO applicationConfigurationDTO = new ApplicationConfigurationDTO(
				"Test Estimate Hub", "Security", "N/A", 0, "LOW");
		TestFactory.recordTestStep(String.format("Persisting application configuration: %s", applicationConfigurationDTO));
		Assertions.assertThatThrownBy(() -> {
			applicationConfigurationService.save(applicationConfigurationDTO);
		}).isInstanceOf(ApplicationConfigurationTransactionException.class)
				.hasMessage("Base test script count cannot be lower than 1.");
	}
}
