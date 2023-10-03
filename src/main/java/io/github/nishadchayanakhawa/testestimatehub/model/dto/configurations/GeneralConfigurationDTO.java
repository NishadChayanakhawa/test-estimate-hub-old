package io.github.nishadchayanakhawa.testestimatehub.model.dto.configurations;

//import section
//lombok
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * <b>Class Name</b>: GeneralConfigurationDTO<br>
 * <b>Description</b>: DTO representation for general configuration entity.<br>
 * @author nishad.chayanakhawa
 */
@ToString
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GeneralConfigurationDTO {
	//id
	private Long id;
	//test design productivity
	private double testDesignProductivityForVeryLowComplexity;
	private double testDesignProductivityForLowComplexity;
	private double testDesignProductivityForMediumComplexity;
	private double testDesignProductivityForHighComplexity;
	private double testDesignProductivityForVeryHighComplexity;
	//test execution productivity
	private double testExecutionProductivityForVeryLowComplexity;
	private double testExecutionProductivityForLowComplexity;
	private double testExecutionProductivityForMediumComplexity;
	private double testExecutionProductivityForHighComplexity;
	private double testExecutionProductivityForVeryHighComplexity;

	//test configuration complexity weightage
	private double testConfigurationComplexityPercentage;
	//test data complexity weightage
	private double testDataComplexityPercentage;
	//test transaction complexity weightage
	private double testTransactionComplexityPercentage;
	//test validation complexity weightage
	private double testValidationComplexityPercentage;
}
