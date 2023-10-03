package io.github.nishadchayanakhawa.testestimatehub.unittests.modelTests;

import org.assertj.core.api.Assertions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Test;

import io.github.nishadchayanakhawa.testestimatehub.TestEstimateHubApplication;
import io.github.nishadchayanakhawa.testestimatehub.model.dto.configurations.GeneralConfigurationDTO;
import io.github.nishadchayanakhawa.testestimatehub.services.configurations.GeneralConfigurationService;
import io.github.nishadchayanakhawa.testestimatehub.services.configurations.exceptions.GeneralConfigurationTransactionException;
import io.nishadc.automationtestingframework.testngcustomization.TestFactory;

@SpringBootTest(classes = TestEstimateHubApplication.class, webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class GeneralConfigurationServiceTests extends AbstractTestNGSpringContextTests {
	@Autowired
	private GeneralConfigurationService generalConfigurationService;
	
	@Test
	public void addGeneralConfigurationTest() {
		TestFactory.recordTest("General Configuration: Update");
		GeneralConfigurationDTO generalConfigurationDTO = new GeneralConfigurationDTO(null, 21d, 18d, 15d, 12d, 9d, 18d, 15d, 12d, 9d, 6d, 10d, 20d,
				60d, 10d);
		TestFactory.recordTestStep(String.format("Persisting general configuration: %s", generalConfigurationDTO));
		GeneralConfigurationDTO savedGeneralConfigurationDTO = generalConfigurationService
				.save(generalConfigurationDTO);
		TestFactory.recordTestStep(String.format("Persisted general configuration: %s", savedGeneralConfigurationDTO));
		Assertions.assertThat(savedGeneralConfigurationDTO.getTestTransactionComplexityPercentage()).isEqualTo(60d);
	}
	
	@Test
	public void addInvalidGeneralConfigurationTest() {
		TestFactory.recordTest("General Configuration: Weightage total not 100.0");
		GeneralConfigurationDTO generalConfigurationDTO = new GeneralConfigurationDTO(null, 21d, 18d, 15d, 12d, 9d, 18d, 15d, 12d, 9d, 6d, 10d, 20d,
				60.1d, 10d);
		TestFactory.recordTestStep(String.format("Persisting general configuration: %s", generalConfigurationDTO));
		Assertions.assertThatThrownBy(() -> generalConfigurationService
				.save(generalConfigurationDTO)).isExactlyInstanceOf(GeneralConfigurationTransactionException.class)
		.hasMessage("Test complexity weightage percentages must add up to 100.");
	}
	
	@Test
	public void addInvalidGeneralConfigurationTransactionTest() {
		TestFactory.recordTest("General Configuration: Values");
		GeneralConfigurationDTO generalConfigurationDTO = new GeneralConfigurationDTO(null, 0d, 18d, 15d, 12d, 9d, 18d, 15d, 12d, 9d, 6d, 10d, 20d,
				60d, 10d);
		TestFactory.recordTestStep(String.format("Persisting general configuration: %s", generalConfigurationDTO));
		Assertions.assertThatThrownBy(() -> generalConfigurationService
				.save(generalConfigurationDTO)).isExactlyInstanceOf(GeneralConfigurationTransactionException.class)
		.hasMessageContaining("Productivity value cannot be less than 1.");
	}
}
