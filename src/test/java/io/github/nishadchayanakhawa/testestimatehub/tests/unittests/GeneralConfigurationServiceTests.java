package io.github.nishadchayanakhawa.testestimatehub.tests.unittests;

import java.util.Map;
import org.assertj.core.api.Assertions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Test;
import io.github.nishadchayanakhawa.testestimatehub.TestEstimateHubApplication;
import io.github.nishadchayanakhawa.testestimatehub.model.Complexity;
import io.github.nishadchayanakhawa.testestimatehub.model.dto.GeneralConfigurationDTO;
import io.github.nishadchayanakhawa.testestimatehub.services.GeneralConfigurationService;
import io.github.nishadchayanakhawa.testestimatehub.services.configurations.exceptions.GeneralConfigurationTransactionException;
import io.nishadc.automationtestingframework.testngcustomization.TestFactory;

@SpringBootTest(classes = TestEstimateHubApplication.class, webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class GeneralConfigurationServiceTests extends AbstractTestNGSpringContextTests {
	@Autowired
	private GeneralConfigurationService generalConfigurationService;
	
	private Map<Complexity,Double> testDesignProductivity=Map.of(Complexity.VERY_LOW, 21d, Complexity.LOW, 18d,
			Complexity.MEDIUM, 15d, Complexity.HIGH, 12d, Complexity.VERY_HIGH, 9d);
	private Map<Complexity,Double> testExecutionProductivity=Map.of(Complexity.VERY_LOW, 18d, Complexity.LOW, 15d,
			Complexity.MEDIUM, 12d, Complexity.HIGH, 9d, Complexity.VERY_HIGH, 6d);
	
	@Test
	public void addGeneralConfigurationTest() {
		TestFactory.recordTest("General Configuration: Update");
		GeneralConfigurationDTO generalConfigurationDTO = new GeneralConfigurationDTO(null, testDesignProductivity, testExecutionProductivity, 10d, 20d,
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
		GeneralConfigurationDTO generalConfigurationDTO = new GeneralConfigurationDTO(null, testDesignProductivity, testExecutionProductivity, 10d, 20d,
				60.1d, 10d);
		TestFactory.recordTestStep(String.format("Persisting general configuration: %s", generalConfigurationDTO));
		Assertions.assertThatThrownBy(() -> generalConfigurationService
				.save(generalConfigurationDTO)).isExactlyInstanceOf(GeneralConfigurationTransactionException.class)
		.hasMessage("Test complexity weightage percentages must add up to 100.");
	}
}
