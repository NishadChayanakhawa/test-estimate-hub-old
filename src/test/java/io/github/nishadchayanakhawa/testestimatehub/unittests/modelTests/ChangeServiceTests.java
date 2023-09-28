package io.github.nishadchayanakhawa.testestimatehub.unittests.modelTests;

import java.time.LocalDate;
import java.util.List;
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
import io.github.nishadchayanakhawa.testestimatehub.model.dto.records.ChangeDTO;
import io.github.nishadchayanakhawa.testestimatehub.model.dto.records.ReleaseDTO;
import io.github.nishadchayanakhawa.testestimatehub.model.dto.records.RequirementDTO;
import io.github.nishadchayanakhawa.testestimatehub.services.records.exceptions.DuplicateChangeException;
import io.github.nishadchayanakhawa.testestimatehub.services.records.exceptions.DuplicateChangeImpactException;
import io.nishadc.automationtestingframework.testngcustomization.TestFactory;
import io.github.nishadchayanakhawa.testestimatehub.TestEstimateHubApplication;

@SpringBootTest(classes = TestEstimateHubApplication.class, webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class ChangeServiceTests extends AbstractTestNGSpringContextTests {
	@Autowired
	private ReleaseService releaseService;

	private static ReleaseDTO releaseToSave = new ReleaseDTO(null, "DEC-2023", "December 2023 Major Release",
			LocalDate.now(), LocalDate.now().plusDays(7));

	@Autowired
	private ChangeService changeService;

	private static ChangeDTO changeToSave = new ChangeDTO(null, null, null, "ID", "IntelliDash", 1L, null,
			LocalDate.now(), LocalDate.now().plusDays(7),
			List.of(new RequirementDTO("BN01","User Management","LOW")),
			Set.of(new ApplicationConfigurationDTO(1L,null,null,null,0,null,null)));
	private static Long savedChangeId = 0L;
	private static Long savedReleaseId=0L;

	@BeforeClass(alwaysRun = true)
	public void setup() {
		ReleaseDTO releaseSavedDTO = this.releaseService.save(releaseToSave);
		savedReleaseId=releaseSavedDTO.getId();
		changeToSave.setReleaseId(savedReleaseId);
	}

	@Test(enabled=true)
	public void addChange() {
		TestFactory.recordTest("Change: Add Transaction");
		TestFactory.recordTestStep(String.format("Saving change: %s", changeToSave));
		ChangeDTO changeSavedDTO = this.changeService.save(changeToSave);
		TestFactory.recordTestStep(String.format("Saved change: %s", changeSavedDTO));
		Assertions.assertThat(changeSavedDTO.getSummary()).isEqualTo(ChangeServiceTests.changeToSave.getSummary());
		Assertions.assertThat(changeSavedDTO.getId()).isNotNull().isPositive();
		ChangeServiceTests.savedChangeId = changeSavedDTO.getId();
	}
	
	@Test
	public void addChangeWithDuplicateImpact() {
		TestFactory.recordTest("Change: Duplicate Change Impact");
		ChangeDTO changeToSave = new ChangeDTO(null, null, null, "DM", "Dummy", 1L, null,
				LocalDate.now(), LocalDate.now().plusDays(7),
				List.of(new RequirementDTO("BN01","User Management","LOW")),
				Set.of(new ApplicationConfigurationDTO(1L,null,null,null,0,null,null),new ApplicationConfigurationDTO(1L,null,null,null,0,null,null)));
		changeToSave.setReleaseId(savedReleaseId);
		TestFactory.recordTestStep(String.format("Saving change: %s", changeToSave));
		Assertions.assertThatThrownBy(() -> {
			this.changeService.save(changeToSave);
		}).isInstanceOf(DuplicateChangeImpactException.class).hasMessage("Please remove duplicate entries from impacted area.");
	}

	@Test(enabled=true,dependsOnMethods = { "addChange" })
	public void duplicateChange() {
		TestFactory.recordTest("Change: Duplicate add transaction");
		TestFactory.recordTestStep(String.format("Saving duplicate change: %s", changeToSave));
		Assertions.assertThatThrownBy(() -> {
			this.changeService.save(changeToSave);
		}).isInstanceOf(DuplicateChangeException.class).hasMessage("Change 'ID' already exists.");
	}

	@Test(enabled=true,dependsOnMethods = { "addChange" })
	public void changeList() {
		TestFactory.recordTest("Change: Get All");
		List<ChangeDTO> changes = this.changeService.getAll();
		TestFactory.recordTestStep(String.format("Changes: %s", changes));
		Assertions.assertThat(changes).isNotEmpty();
	}

	@Test(enabled=true,dependsOnMethods = { "changeList" })
	public void getChange() {
		TestFactory.recordTest("Change: Get");
		ChangeDTO change = this.changeService.get(ChangeServiceTests.savedChangeId);
		TestFactory.recordTestStep(String.format("Change: %s", change));
		Assertions.assertThat(change.getSummary()).isEqualTo(ChangeServiceTests.changeToSave.getSummary());
	}

	@Test(enabled=true,dependsOnMethods = { "getChange" })
	public void deleteChange() {
		TestFactory.recordTest("Change: Delete");
		Assertions.assertThatNoException().isThrownBy(() -> {
			this.changeService.delete(
					new ChangeDTO(ChangeServiceTests.savedChangeId, null, null, null, null, null, null, null, null,null,null));
		});
	}
}
