package io.github.nishadchayanakhawa.testestimatehub.unittests.modelTests;

import java.util.List;

import org.assertj.core.api.Assertions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Test;

import io.github.nishadchayanakhawa.testestimatehub.services.configurations.TestTypeService;
import io.github.nishadchayanakhawa.testestimatehub.model.dto.configurations.TestTypeDTO;
import io.github.nishadchayanakhawa.testestimatehub.services.configurations.exceptions.DuplicateTestTypeException;
import io.github.nishadchayanakhawa.testestimatehub.services.configurations.exceptions.TestTypeTransactionException;
import io.nishadc.automationtestingframework.testngcustomization.TestFactory;
import io.github.nishadchayanakhawa.testestimatehub.TestEstimateHubApplication;

@SpringBootTest(classes = TestEstimateHubApplication.class, webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class TestTypeServiceTests extends AbstractTestNGSpringContextTests {
	@Autowired
	private TestTypeService testTypeService;
	
	private static TestTypeDTO testTypeToSave=new TestTypeDTO(null,"Add Test Type",21d,10d,10d);
	private static Long savedTestTypeId=0L;
	
	@Test
	public void addTestType() {
		TestFactory.recordTest("Add test type");
		TestFactory.recordTestStep(String.format("Saving test type: %s", testTypeToSave));
		TestTypeDTO testTypeSavedDTO=this.testTypeService.save(testTypeToSave);
		TestFactory.recordTestStep(String.format("Saved test type: %s", testTypeSavedDTO));
		Assertions.assertThat(testTypeSavedDTO.getName()).isEqualTo(TestTypeServiceTests.testTypeToSave.getName());
		Assertions.assertThat(testTypeSavedDTO.getId()).isNotNull().isGreaterThan(0);
		TestTypeServiceTests.savedTestTypeId=testTypeSavedDTO.getId();
	}
	
	@Test(dependsOnMethods= {"addTestType"})
	public void duplicateTestType() {
		TestFactory.recordTest("Add duplicate test type");
		TestFactory.recordTestStep(String.format("Saving suplicate test type: %s", testTypeToSave));
		Assertions.assertThatThrownBy(() -> {
			this.testTypeService.save(testTypeToSave);
		}).isInstanceOf(DuplicateTestTypeException.class)
		.hasMessage("Test type 'Add Test Type' already exists.");
	}
	
	@Test
	public void transactionErrorTestType() {
		TestFactory.recordTest("Add test type to cause transaction error");
		TestTypeDTO testTypeToSaveDTO=new TestTypeDTO(null,"Error Test Type",-21d,101d,10d);
		TestFactory.recordTestStep(String.format("Saving error injected test type: %s", testTypeToSaveDTO));
		Assertions.assertThatThrownBy(() -> {
			this.testTypeService.save(testTypeToSaveDTO);
		}).isInstanceOf(TestTypeTransactionException.class)
		.hasMessageContainingAll(
				"Percentage cannot be lower than 0%",
				"Percentage cannot be more than 100%");
	}
	
	@Test(dependsOnMethods= {"addTestType"})
	public void testTypeList() {
		TestFactory.recordTest("Get list of all test types");
		List<TestTypeDTO> testTypes=this.testTypeService.getAll();
		TestFactory.recordTestStep(String.format("Test types: ", testTypes));
		Assertions.assertThat(testTypes).isNotEmpty();
	}
	
	@Test(dependsOnMethods= {"testTypeList"})
	public void getTestType() {
		TestFactory.recordTest("Get test types");
		TestTypeDTO testType=this.testTypeService.get(TestTypeServiceTests.savedTestTypeId);
		TestFactory.recordTestStep(String.format("Teest type: ", testType));
		Assertions.assertThat(testType.getName()).isEqualTo(TestTypeServiceTests.testTypeToSave.getName());
	}
}
