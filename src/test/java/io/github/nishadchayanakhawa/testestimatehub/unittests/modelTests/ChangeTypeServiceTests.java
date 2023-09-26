package io.github.nishadchayanakhawa.testestimatehub.unittests.modelTests;

import java.util.List;

import org.assertj.core.api.Assertions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Test;

import io.github.nishadchayanakhawa.testestimatehub.services.configurations.ChangeTypeService;
import io.github.nishadchayanakhawa.testestimatehub.model.dto.configurations.ChangeTypeDTO;
import io.github.nishadchayanakhawa.testestimatehub.services.configurations.exceptions.DuplicateChangeTypeException;
import io.github.nishadchayanakhawa.testestimatehub.services.configurations.exceptions.ChangeTypeTransactionException;
import io.nishadc.automationtestingframework.testngcustomization.TestFactory;
import io.github.nishadchayanakhawa.testestimatehub.TestEstimateHubApplication;

@SpringBootTest(classes = TestEstimateHubApplication.class, webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class ChangeTypeServiceTests extends AbstractTestNGSpringContextTests {
	@Autowired
	private ChangeTypeService changeTypeService;
	
	private static ChangeTypeDTO changeTypeToSave=new ChangeTypeDTO(null,"Add Change Type",1.2d,21d,10d,10d);
	private static Long savedChangeTypeId=0L;
	
	@Test
	public void addChangeType() {
		TestFactory.recordTest("Add change type");
		TestFactory.recordTestStep(String.format("Saving change type: %s", changeTypeToSave));
		ChangeTypeDTO changeTypeSavedDTO=this.changeTypeService.save(changeTypeToSave);
		TestFactory.recordTestStep(String.format("Saved change type: %s", changeTypeSavedDTO));
		Assertions.assertThat(changeTypeSavedDTO.getName()).isEqualTo(ChangeTypeServiceTests.changeTypeToSave.getName());
		Assertions.assertThat(changeTypeSavedDTO.getId()).isNotNull().isPositive();
		ChangeTypeServiceTests.savedChangeTypeId=changeTypeSavedDTO.getId();
	}
	
	@Test(dependsOnMethods= {"addChangeType"})
	public void duplicateChangeType() {
		TestFactory.recordTest("Add duplicate change type");
		TestFactory.recordTestStep(String.format("Saving suplicate change type: %s", changeTypeToSave));
		Assertions.assertThatThrownBy(() -> {
			this.changeTypeService.save(changeTypeToSave);
		}).isInstanceOf(DuplicateChangeTypeException.class)
		.hasMessage("Change type 'Add Change Type' already exists.");
	}
	
	@Test
	public void transactionErrorChangeType() {
		TestFactory.recordTest("Add change type to cause transaction error");
		ChangeTypeDTO changeTypeToSaveDTO=new ChangeTypeDTO(null,"Error Change Type",0.2,-21d,101d,10d);
		TestFactory.recordTestStep(String.format("Saving error injected change type: %s", changeTypeToSaveDTO));
		Assertions.assertThatThrownBy(() -> {
			this.changeTypeService.save(changeTypeToSaveDTO);
		}).isInstanceOf(ChangeTypeTransactionException.class)
		.hasMessageContainingAll(
				"Percentage cannot be lower than 0%",
				"Percentage cannot be more than 100%");
	}
	
	@Test(dependsOnMethods= {"addChangeType"})
	public void changeTypeList() {
		TestFactory.recordTest("Get list of all change types");
		List<ChangeTypeDTO> changeTypes=this.changeTypeService.getAll();
		TestFactory.recordTestStep(String.format("Change types: %s", changeTypes));
		Assertions.assertThat(changeTypes).isNotEmpty();
	}
	
	@Test(dependsOnMethods= {"changeTypeList"})
	public void getChangeType() {
		TestFactory.recordTest("Get change type");
		ChangeTypeDTO changeType=this.changeTypeService.get(ChangeTypeServiceTests.savedChangeTypeId);
		TestFactory.recordTestStep(String.format("Teest type: %s", changeType));
		Assertions.assertThat(changeType.getName()).isEqualTo(ChangeTypeServiceTests.changeTypeToSave.getName());
	}
	
	@Test(dependsOnMethods= {"getChangeType"})
	public void deleteChangeType() {
		TestFactory.recordTest("Delete change types");
		Assertions.assertThatNoException().isThrownBy(() -> {
			this.changeTypeService.delete(new ChangeTypeDTO(ChangeTypeServiceTests.savedChangeTypeId,null,0,0,0,0));
		});
	}
}
