package io.github.nishadchayanakhawa.testestimatehub.configurations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import io.github.nishadchayanakhawa.testestimatehub.services.configurations.ApplicationConfigurationService;
import io.github.nishadchayanakhawa.testestimatehub.services.configurations.TestTypeService;
import io.github.nishadchayanakhawa.testestimatehub.services.records.ChangeService;
import io.github.nishadchayanakhawa.testestimatehub.services.records.ReleaseService;
import io.github.nishadchayanakhawa.testestimatehub.services.configurations.ChangeTypeService;
import io.github.nishadchayanakhawa.testestimatehub.services.configurations.GeneralConfigurationService;
import io.github.nishadchayanakhawa.testestimatehub.model.configurations.Complexity;
import io.github.nishadchayanakhawa.testestimatehub.model.dto.configurations.ApplicationConfigurationDTO;
import io.github.nishadchayanakhawa.testestimatehub.model.dto.configurations.TestTypeDTO;
import io.github.nishadchayanakhawa.testestimatehub.model.dto.configurations.GeneralConfigurationDTO;
import io.github.nishadchayanakhawa.testestimatehub.model.dto.records.ChangeDTO;
import io.github.nishadchayanakhawa.testestimatehub.model.dto.records.ReleaseDTO;
import io.github.nishadchayanakhawa.testestimatehub.model.dto.records.RequirementDTO;
import io.github.nishadchayanakhawa.testestimatehub.model.dto.configurations.ChangeTypeDTO;
import io.github.nishadchayanakhawa.testestimatehub.model.dto.records.UseCaseDTO;

@Component
public class CommandLineAppStartupRunner implements CommandLineRunner {
	private static final Logger logger = LoggerFactory.getLogger(CommandLineAppStartupRunner.class);

	@Value("${server.port}")
	private int serverPort;

	@Autowired
	private ApplicationConfigurationService applicationConfigurationService;

	@Autowired
	private TestTypeService testTypeService;

	@Autowired
	private ChangeTypeService changeTypeService;

	@Autowired
	private GeneralConfigurationService generalConfigurationService;

	@Autowired
	private ChangeService changeService;

	@Autowired
	private ReleaseService releaseService;

	@Override
	public void run(String... args) throws Exception {
		CommandLineAppStartupRunner.logger.info("Application started!!!");
		CommandLineAppStartupRunner.logger.info("Navigate to http://localhost:{}", serverPort);

		this.loadApplicationConfiguration();
		this.loadTestType();
		this.loadChangeType();
		this.loadGeneralConfiguration();
		this.loadRelease();
		this.loadChange();
	}

	private void loadApplicationConfiguration() {
		ApplicationConfigurationDTO applicationConfigurationDTO = new ApplicationConfigurationDTO(null, "Application1",
				"Module1", "Sub-Module1", 6, "LOW", null);
		ApplicationConfigurationDTO savedApplicationConfigurationDTO = applicationConfigurationService
				.save(applicationConfigurationDTO);
		applicationConfigurationService
				.save(new ApplicationConfigurationDTO(null, "Application1", "Module1", "Sub-Module2", 6, "LOW", null));
		logger.info("App Configuration: {}", savedApplicationConfigurationDTO);
	}

	private void loadTestType() {
		if (this.testTypeService.getAll().isEmpty()) {
			Arrays.asList(new TestTypeDTO("System Integration Testing", 100d, 20d, 20d),
					new TestTypeDTO("Regression Testing", 21d, 10d, 10d),
					new TestTypeDTO("User Acceptance Testing", 10d, 5d, 5d)).stream().forEach(testTypeDTO -> {
						TestTypeDTO testTypeSavedDTO = this.testTypeService.save(testTypeDTO);
						logger.info("Test Type Loaded: {}", testTypeSavedDTO);
					});
		}
	}

	private void loadChangeType() {
		if (this.changeTypeService.getAll().isEmpty()) {
			Arrays.asList(new ChangeTypeDTO(null, "Significant", 1.4d, 20d, 20d, 10d),
					new ChangeTypeDTO(null, "Major", 1.2d, 15d, 15d, 10d),
					new ChangeTypeDTO(null, "Minor", 1.0d, 0d, 10d, 5d),
					new ChangeTypeDTO(null, "Incident", 0.8d, 0d, 5d, 5d)).stream().forEach(changeTypeDTO -> {
						ChangeTypeDTO changeTypeSavedDTO = this.changeTypeService.save(changeTypeDTO);
						logger.info("Change Type Loaded: {}", changeTypeSavedDTO);
					});
		}
	}

	private void loadRelease() {
		if (this.releaseService.getAll().isEmpty()) {
			ReleaseDTO releaseToSave = new ReleaseDTO("OCT-2023", "October 2023 Major Release", LocalDate.now(),
					LocalDate.now().plusDays(7));
			ReleaseDTO releaseSavedDTO = this.releaseService.save(releaseToSave);
			logger.info("Release saved: {}", releaseSavedDTO);
		}
	}

	private void loadChange() {
		if (this.changeService.getAll().isEmpty()) {
			ChangeDTO changeToSave = new ChangeDTO(null, 1L, null, "TEH", "Test Estimate Hub", 1L, null,
					LocalDate.now(), LocalDate.now().plusDays(7),
					Set.of(new RequirementDTO("BN01", "User Management", "LOW"),
							new RequirementDTO("BN02", "Configuration", "MEDIUM")),
					Set.of(new ApplicationConfigurationDTO(1L, null, null, null, 0, null, null),
							new ApplicationConfigurationDTO(2L, null, null, null, 0, null, null)),
					null,0,0,0,0,0,0,0);
			ChangeDTO changeSavedDTO = this.changeService.save(changeToSave);

			changeSavedDTO = this.changeService.get(changeSavedDTO.getId());
			RequirementDTO requirement = new RequirementDTO("BN03", "Configuration", "MEDIUM");
			requirement.setUseCases(Set.of(new UseCaseDTO("1st Use Case", 1L, 2, "LOW", "LOW", "LOW", "LOW",
					Set.of(new TestTypeDTO(1L), new TestTypeDTO(2L)))));
			changeSavedDTO.getRequirements().add(requirement);
			changeSavedDTO = this.changeService.save(changeSavedDTO);
			this.changeService.generateEstimates(changeSavedDTO.getId());
			logger.info("Change saved: {}", changeSavedDTO);
		}
	}

	private void loadGeneralConfiguration() {
		if (this.generalConfigurationService.get().getId() == null) {
			Map<Complexity, Double> testDesignProductivity = Map.of(Complexity.VERY_LOW, 21d, Complexity.LOW, 18d,
					Complexity.MEDIUM, 15d, Complexity.HIGH, 12d, Complexity.VERY_HIGH, 9d);
			Map<Complexity, Double> testExecutionProductivity = Map.of(Complexity.VERY_LOW, 18d, Complexity.LOW, 15d,
					Complexity.MEDIUM, 12d, Complexity.HIGH, 9d, Complexity.VERY_HIGH, 6d);
			GeneralConfigurationDTO savedGeneralConfiguration = this.generalConfigurationService
					.save(new GeneralConfigurationDTO(null, testDesignProductivity, testExecutionProductivity, 10d, 20d,
							40d, 30d));
			logger.info("General Configuration saved: {}", savedGeneralConfiguration);
		}
	}
}