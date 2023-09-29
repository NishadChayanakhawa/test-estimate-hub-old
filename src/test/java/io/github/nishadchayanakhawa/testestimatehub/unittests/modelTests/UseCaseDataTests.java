package io.github.nishadchayanakhawa.testestimatehub.unittests.modelTests;

import java.util.HashSet;
import java.time.LocalDate;
import java.util.Set;
import org.assertj.core.api.Assertions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import io.github.nishadchayanakhawa.testestimatehub.services.records.ChangeService;
import io.github.nishadchayanakhawa.testestimatehub.services.records.ReleaseService;
import io.github.nishadchayanakhawa.testestimatehub.model.dto.configurations.ApplicationConfigurationDTO;
import io.github.nishadchayanakhawa.testestimatehub.model.dto.configurations.TestTypeDTO;
import io.github.nishadchayanakhawa.testestimatehub.model.dto.records.ChangeDTO;
import io.github.nishadchayanakhawa.testestimatehub.model.dto.records.ReleaseDTO;
import io.github.nishadchayanakhawa.testestimatehub.model.dto.records.RequirementDTO;
import io.github.nishadchayanakhawa.testestimatehub.model.dto.records.UseCaseDTO;
import io.nishadc.automationtestingframework.testngcustomization.TestFactory;
import io.github.nishadchayanakhawa.testestimatehub.TestEstimateHubApplication;

@SpringBootTest(classes = TestEstimateHubApplication.class, webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class UseCaseDataTests extends AbstractTestNGSpringContextTests {
	@Autowired
	private ReleaseService releaseService;

	private static ReleaseDTO releaseToSave = new ReleaseDTO(null, "Feb-2024", "February 2024 Major Release",
			LocalDate.now(), LocalDate.now().plusDays(7));

	@Autowired
	private ChangeService changeService;

	private static ChangeDTO changeToSave = new ChangeDTO(null, null, null, "AI", "Artificial Intelligence", 2L, null,
			LocalDate.now(), LocalDate.now().plusDays(7), Set.of(new RequirementDTO("BN01", "User Management", "LOW")),
			Set.of(new ApplicationConfigurationDTO(1L, null, null, null, 0, null, null)));
	private static ChangeDTO savedChange;

	@BeforeClass(alwaysRun = true)
	public void setup() {
		ReleaseDTO releaseSavedDTO = this.releaseService.save(releaseToSave);
		changeToSave.setReleaseId(releaseSavedDTO.getId());
		ChangeDTO savedChange = this.changeService.save(changeToSave);
		UseCaseDataTests.savedChange = this.changeService.get(savedChange.getId());
	}

	@Test
	public void validatePrerequisites() {
		TestFactory.recordTest("Use Case: Pre-requisites");
		Assertions.assertThat(savedChange.getId()).isPositive();
		TestFactory.recordTestStep(String.format("Change : %s", savedChange));
	}

	@Test
	public void addUseCase() {
		TestFactory.recordTest("Use Case: Add");
		UseCaseDTO useCase1 = new UseCaseDTO("1st Use Case", 1L, 2, "LOW", "LOW", "LOW", "LOW",
				Set.of(new TestTypeDTO(1L, null, 0d, 0d, 0d), new TestTypeDTO(2L, null, 0d, 0d, 0d)));
		UseCaseDTO useCase2 = new UseCaseDTO("2nd Use Case", 1L, 2, "MEDIUM", "MEDIUM", "MEDIUM", "MEDIUM",
				Set.of(new TestTypeDTO(1L, null, 0d, 0d, 0d)));
		Set<UseCaseDTO> useCases = new HashSet<>();
		useCases.add(useCase1);
		useCases.add(useCase2);
		RequirementDTO requirement=new RequirementDTO();
		requirement.setId(savedChange.getRequirements().stream().toList().get(0).getId());
		requirement.setUseCases(useCases);
		TestFactory.recordTestStep(String.format("Saving with use case: %s", requirement));
		
		RequirementDTO savedRequirement=this.changeService.saveUseCases(requirement);
		savedRequirement=this.changeService.getRequirement(savedRequirement.getId());
		
		TestFactory.recordTestStep(String.format("Saved with use case: %s", savedRequirement));
		Assertions.assertThat(savedRequirement.getUseCases()).hasSize(2);
	}

	@Test(dependsOnMethods = { "addUseCase" })
	public void addAnotherUseCase() {
		TestFactory.recordTest("Use Case: Add Another");
		savedChange = this.changeService.get(UseCaseDataTests.savedChange.getId());
		
		UseCaseDTO useCase1 = new UseCaseDTO("3rd Use Case", 1L, 2, "LOW", "LOW", "LOW", "LOW",
				Set.of(new TestTypeDTO(1L, null, 0d, 0d, 0d), new TestTypeDTO(2L, null, 0d, 0d, 0d)));
		UseCaseDataTests.savedChange.getRequirements().stream()
				.forEach(requirement -> requirement.getUseCases().add(useCase1));
		
		RequirementDTO requirement=new RequirementDTO();
		requirement.setId(savedChange.getRequirements().stream().toList().get(0).getId());
		requirement.setUseCases(savedChange.getRequirements().stream().toList().get(0).getUseCases());
		requirement.getUseCases().add(useCase1);
		
		TestFactory.recordTestStep(String.format("Saving with use case: %s", requirement));
		RequirementDTO savedRequirement=this.changeService.saveUseCases(requirement);
		savedRequirement=this.changeService.getRequirement(savedRequirement.getId());
		TestFactory.recordTestStep(String.format("Saved with use case: %s", savedRequirement));
		
		Assertions.assertThat(savedRequirement.getUseCases()).hasSize(3);
	}

	@Test(dependsOnMethods = { "addAnotherUseCase" })
	public void deleteUseCase() {
		TestFactory.recordTest("Use Case: Delete");
		
		savedChange = this.changeService.get(UseCaseDataTests.savedChange.getId());
		RequirementDTO requirement=new RequirementDTO();
		requirement.setId(savedChange.getRequirements().stream().toList().get(0).getId());
		requirement.setUseCases(savedChange.getRequirements().stream().toList().get(0).getUseCases());
		
		requirement.getUseCases().removeIf(useCase -> "1st Use Case".equals(useCase.getSummary())
						|| "3rd Use Case".equals(useCase.getSummary()));
		
		TestFactory.recordTestStep(String.format("Saving with use case: %s", requirement));
		RequirementDTO savedRequirement=this.changeService.saveUseCases(requirement);
		savedRequirement=this.changeService.getRequirement(savedRequirement.getId());
		
		TestFactory.recordTestStep(String.format("Saved with use case: %s", savedRequirement));
		Assertions.assertThat(savedRequirement.getUseCases()).hasSize(1);
	}

	@Test(dependsOnMethods = { "deleteUseCase" })
	public void updateUseCase() {
		TestFactory.recordTest("Use Case: Update");
		savedChange = this.changeService.get(UseCaseDataTests.savedChange.getId());
		RequirementDTO requirement=new RequirementDTO();
		requirement.setId(savedChange.getRequirements().stream().toList().get(0).getId());
		requirement.setUseCases(savedChange.getRequirements().stream().toList().get(0).getUseCases());
		
		requirement.getUseCases().forEach(useCase -> useCase.setSummary("This should be 1st"));
		
		TestFactory.recordTestStep(String.format("Saving with use case: %s", requirement));
		RequirementDTO savedRequirement=this.changeService.saveUseCases(requirement);
		savedRequirement=this.changeService.getRequirement(savedRequirement.getId());
		
		TestFactory.recordTestStep(String.format("Saved with use case: %s", savedRequirement));
		Assertions.assertThat(savedRequirement.getUseCases().stream().toList().get(0).getSummary())
			.isEqualTo("This should be 1st");
	}
}
