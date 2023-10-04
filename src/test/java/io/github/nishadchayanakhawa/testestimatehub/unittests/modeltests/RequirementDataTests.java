package io.github.nishadchayanakhawa.testestimatehub.unittests.modeltests;

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
import io.github.nishadchayanakhawa.testestimatehub.services.records.exceptions.DuplicateRequirementException;
import io.github.nishadchayanakhawa.testestimatehub.model.dto.configurations.ApplicationConfigurationDTO;
import io.github.nishadchayanakhawa.testestimatehub.model.dto.records.ChangeDTO;
import io.github.nishadchayanakhawa.testestimatehub.model.dto.records.ReleaseDTO;
import io.github.nishadchayanakhawa.testestimatehub.model.dto.records.RequirementDTO;
import io.nishadc.automationtestingframework.testngcustomization.TestFactory;
import io.github.nishadchayanakhawa.testestimatehub.TestEstimateHubApplication;

@SpringBootTest(classes = TestEstimateHubApplication.class, webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class RequirementDataTests extends AbstractTestNGSpringContextTests {
	@Autowired
	private ReleaseService releaseService;

	private static ReleaseDTO releaseToSave = new ReleaseDTO(null, "JAN-2024", "January 2024 Major Release",
			LocalDate.now(), LocalDate.now().plusDays(7));

	@Autowired
	private ChangeService changeService;

	private static ChangeDTO changeToSave = new ChangeDTO(null, null, null, "TV", "Task Vault", 2L, null,
			LocalDate.now(), LocalDate.now().plusDays(7),
			Set.of(new RequirementDTO("BN01","User Management","LOW")),
			Set.of(new ApplicationConfigurationDTO(1L)),null,0,0,0,0,0,0,0);
	private static ChangeDTO savedChange;

	@BeforeClass(alwaysRun = true)
	public void setup() {
		ReleaseDTO releaseSavedDTO = this.releaseService.save(releaseToSave);
		changeToSave.setReleaseId(releaseSavedDTO.getId());
		ChangeDTO savedChange=this.changeService.save(changeToSave);
		RequirementDataTests.savedChange=this.changeService.get(savedChange.getId());
	}

	@Test
	public void validatePrerequisites() {
		TestFactory.recordTest("Requirement: Pre-requisites");
		Assertions.assertThat(savedChange.getId()).isPositive();
		TestFactory.recordTestStep(String.format("Change : %s",savedChange));
	}
	
	@Test
	public void addRequirement() {
		TestFactory.recordTest("Requirement: Add new");
		savedChange=this.changeService.get(savedChange.getId());
		savedChange.getRequirements().add(new RequirementDTO("BN02","Configuration","MEDIUM"));
		TestFactory.recordTestStep(String.format("Adding requirement new change: %s",savedChange));
		this.changeService.save(savedChange);
		savedChange=this.changeService.get(savedChange.getId());
		TestFactory.recordTestStep(String.format("Change with added requirement: %s",savedChange));
		Assertions.assertThat(savedChange.getRequirements()).hasSize(2);
	}
	
	@Test(dependsOnMethods= {"addRequirement"})
	public void deleteRequirement() {
		TestFactory.recordTest("Requirement: Delete");
		savedChange.getRequirements().removeIf(requirement -> "BN01".equals(requirement.getIdentifier()));
		this.changeService.save(savedChange);
		savedChange=this.changeService.get(savedChange.getId());
		Assertions.assertThat(savedChange.getRequirements()).hasSize(1);
		TestFactory.recordTestStep(String.format("Change with deleted requirement: %s",savedChange));
	}
	
	@Test(dependsOnMethods= {"deleteRequirement"})
	public void updateRequirement() {
		TestFactory.recordTest("Requirement: Update");
		savedChange.getRequirements().stream()
			.forEach(requirement -> {
				requirement.setComplexityCode("HIGH");
				requirement.setIdentifier("BN01");
			});
		this.changeService.save(savedChange);
		savedChange=this.changeService.get(savedChange.getId());
		Assertions.assertThat(savedChange.getRequirements().stream().toList().get(0).getComplexityDisplayValue()).isEqualTo("High");
		TestFactory.recordTestStep(String.format("Change with updated requirement: %s",savedChange));
	}
	
	@Test(dependsOnMethods= {"updateRequirement"})
	public void duplicateRequirementIdentifier() {
		TestFactory.recordTest("Requirement: Duplicate identifier");
		savedChange.getRequirements().add(new RequirementDTO("BN01","Task Processing","HIGH"));
		Assertions.assertThatThrownBy(() -> {
			this.changeService.save(savedChange);
		}).isInstanceOf(DuplicateRequirementException.class)
		.hasMessage("Please review identifiers used for requirements. Single change record cannot have duplicate identifiers.");
	}
}
