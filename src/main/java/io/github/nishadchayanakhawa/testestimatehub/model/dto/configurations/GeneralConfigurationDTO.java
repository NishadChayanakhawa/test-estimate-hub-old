package io.github.nishadchayanakhawa.testestimatehub.model.dto.configurations;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GeneralConfigurationDTO {
	private Long id;
	private double testDesignProductivityForVeryLowComplexity;
	private double testDesignProductivityForLowComplexity;
	private double testDesignProductivityForMediumComplexity;
	private double testDesignProductivityForHighComplexity;
	private double testDesignProductivityForVeryHighComplexity;

	private double testExecutionProductivityForVeryLowComplexity;
	private double testExecutionProductivityForLowComplexity;
	private double testExecutionProductivityForMediumComplexity;
	private double testExecutionProductivityForHighComplexity;
	private double testExecutionProductivityForVeryHighComplexity;

	private double testConfigurationComplexityPercentage;
	private double testDataComplexityPercentage;
	private double testTransactionComplexityPercentage;
	private double testValidationComplexityPercentage;
}
