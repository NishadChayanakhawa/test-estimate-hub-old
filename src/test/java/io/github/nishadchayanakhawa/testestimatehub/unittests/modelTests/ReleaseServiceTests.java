package io.github.nishadchayanakhawa.testestimatehub.unittests.modelTests;

import java.time.LocalDate;
import java.util.List;

import org.assertj.core.api.Assertions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Test;

import io.github.nishadchayanakhawa.testestimatehub.services.records.ReleaseService;
import io.github.nishadchayanakhawa.testestimatehub.model.dto.records.ReleaseDTO;
import io.github.nishadchayanakhawa.testestimatehub.services.records.exceptions.DuplicateReleaseException;
import io.nishadc.automationtestingframework.testngcustomization.TestFactory;
import io.github.nishadchayanakhawa.testestimatehub.TestEstimateHubApplication;

@SpringBootTest(classes = TestEstimateHubApplication.class, webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class ReleaseServiceTests extends AbstractTestNGSpringContextTests {
	@Autowired
	private ReleaseService releaseService;

	private static ReleaseDTO releaseToSave = new ReleaseDTO(null, "NOV-2023", "November 2023 Major Release",
			LocalDate.now(), LocalDate.now().plusDays(7));
	private static Long savedReleaseId = 0L;

	@Test
	public void addRelease() {
		TestFactory.recordTest("Add Release record");
		TestFactory.recordTestStep(String.format("Saving release: %s", releaseToSave));
		ReleaseDTO releaseSavedDTO = this.releaseService.save(releaseToSave);
		TestFactory.recordTestStep(String.format("Saved release: %s", releaseSavedDTO));
		Assertions.assertThat(releaseSavedDTO.getName()).isEqualTo(ReleaseServiceTests.releaseToSave.getName());
		Assertions.assertThat(releaseSavedDTO.getId()).isNotNull().isPositive();
		ReleaseServiceTests.savedReleaseId = releaseSavedDTO.getId();
	}

	@Test(dependsOnMethods = { "addRelease" })
	public void duplicateRelease() {
		TestFactory.recordTest("Add duplicate release");
		TestFactory.recordTestStep(String.format("Saving duplicate release: %s", releaseToSave));
		Assertions.assertThatThrownBy(() -> {
			this.releaseService.save(releaseToSave);
		}).isInstanceOf(DuplicateReleaseException.class).hasMessage("Release 'NOV-2023' already exists.");
	}

	@Test(dependsOnMethods = { "addRelease" })
	public void releaseList() {
		TestFactory.recordTest("Get list of all releases");
		List<ReleaseDTO> releases = this.releaseService.getAll();
		TestFactory.recordTestStep(String.format("Releases: %s", releases));
		Assertions.assertThat(releases).isNotEmpty();
	}

	@Test(dependsOnMethods = { "releaseList" })
	public void getRelease() {
		TestFactory.recordTest("Get release");
		ReleaseDTO release = this.releaseService.get(ReleaseServiceTests.savedReleaseId);
		TestFactory.recordTestStep(String.format("Release: %s", release));
		Assertions.assertThat(release.getName()).isEqualTo(ReleaseServiceTests.releaseToSave.getName());
	}

	@Test(dependsOnMethods = { "getRelease" })
	public void deleteRelease() {
		TestFactory.recordTest("Delete release");
		Assertions.assertThatNoException().isThrownBy(() -> {
			this.releaseService.delete(new ReleaseDTO(ReleaseServiceTests.savedReleaseId, null,null,null,null));
		});
	}
}
