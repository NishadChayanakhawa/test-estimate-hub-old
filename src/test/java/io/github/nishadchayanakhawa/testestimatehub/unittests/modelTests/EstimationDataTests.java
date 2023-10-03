package io.github.nishadchayanakhawa.testestimatehub.unittests.modelTests;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import org.assertj.core.api.Assertions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import io.github.nishadchayanakhawa.testestimatehub.TestEstimateHubApplication;
import io.github.nishadchayanakhawa.testestimatehub.model.dto.configurations.ApplicationConfigurationDTO;
import io.github.nishadchayanakhawa.testestimatehub.model.dto.configurations.TestTypeDTO;
import io.github.nishadchayanakhawa.testestimatehub.model.dto.records.ChangeDTO;
import io.github.nishadchayanakhawa.testestimatehub.model.dto.records.ReleaseDTO;
import io.github.nishadchayanakhawa.testestimatehub.model.dto.records.RequirementDTO;
import io.github.nishadchayanakhawa.testestimatehub.model.dto.records.UseCaseDTO;
import io.github.nishadchayanakhawa.testestimatehub.services.records.ChangeService;
import io.github.nishadchayanakhawa.testestimatehub.services.records.ReleaseService;
import io.nishadc.automationtestingframework.testngcustomization.TestFactory;

@SpringBootTest(classes = TestEstimateHubApplication.class, webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class EstimationDataTests extends AbstractTestNGSpringContextTests {
	
	@Autowired
	private ReleaseService releaseService;
	
	private static ReleaseDTO releaseToSave = new ReleaseDTO(null, "Mar-2024", "March 2024 Major Release",
			LocalDate.now(), LocalDate.now().plusDays(7));
	
	@Autowired
	private ChangeService changeService;
	
	private static ChangeDTO changeToSave = new ChangeDTO(null, null, null, "TC", "Test Companion", 2L, null,
			LocalDate.now(), LocalDate.now().plusDays(7), Set.of(new RequirementDTO("BN01", "User Management", "LOW")),
			Set.of(new ApplicationConfigurationDTO(1L)),null);
	
	private static ChangeDTO savedChange;
	
	@BeforeClass(alwaysRun = true)
	public void setup() {
		ReleaseDTO releaseSavedDTO = this.releaseService.save(releaseToSave);
		changeToSave.setReleaseId(releaseSavedDTO.getId());
		savedChange = this.changeService.save(changeToSave);
		savedChange = this.changeService.get(savedChange.getId());
		
		UseCaseDTO useCase1 = new UseCaseDTO("1st Use Case", 1L, 2, "LOW", "LOW", "LOW", "LOW",
				Set.of(new TestTypeDTO(1L), new TestTypeDTO(2L)));
		UseCaseDTO useCase2 = new UseCaseDTO("2nd Use Case", 1L, 2, "MEDIUM", "MEDIUM", "MEDIUM", "MEDIUM",
				Set.of(new TestTypeDTO(1L),new TestTypeDTO(3L)));
		Set<UseCaseDTO> useCases = new HashSet<>();
		useCases.add(useCase1);
		useCases.add(useCase2);
		
		RequirementDTO requirement=new RequirementDTO();
		requirement.setId(savedChange.getRequirements().stream().toList().get(0).getId());
		requirement.setUseCases(useCases);
		
		this.changeService.saveUseCases(requirement);
		savedChange=this.changeService.get(savedChange.getId());
	}
	
	@Test
	public void validatePrerequisites() {
		TestFactory.recordTest("Estimation : PreRequisites");
		TestFactory.recordTestStep(String.format("Saved use case: %s", savedChange.getRequirements().stream().toList().get(0).getUseCases()));
		Assertions.assertThat(savedChange.getId()).isPositive();
	}
	
	@Test
	public void calculateEstimates() {
		TestFactory.recordTest("Estimation : Calculate");
		ChangeDTO change=this.changeService.generateEstimates(savedChange.getId());
		TestFactory.recordTestStep(String.format("Saved use case: %s", change.getRequirements().stream().toList().get(0).getUseCases()));
		TestFactory.recordTestStep(String.format("Saved estimations: %s", change.getEstimations()));
	}

}
