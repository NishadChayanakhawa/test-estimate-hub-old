package io.github.nishadchayanakhawa.testestimatehub.model.configurations;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.AttributeOverrides;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "TEH_GENERAL_CONFIGURATION")
@Getter
@Setter
@NoArgsConstructor
public class GeneralConfiguration {
	@Id
	@GeneratedValue
	private Long id; 
	@AttributeOverrides({
		@AttributeOverride(name="forVeryLowComplexity",column=@Column(name="TEST_DESIGN_PRODUCTIVITY_FOR_VERY_LOW_COMPLEXITY")),
		@AttributeOverride(name="forLowComplexity",column=@Column(name="TEST_DESIGN_PRODUCTIVITY_FOR_LOW_COMPLEXITY")),
		@AttributeOverride(name="forMediumComplexity",column=@Column(name="TEST_DESIGN_PRODUCTIVITY_FOR_MEDIUM_COMPLEXITY")),
		@AttributeOverride(name="forHighComplexity",column=@Column(name="TEST_DESIGN_PRODUCTIVITY_FOR_HIGH_COMPLEXITY")),
		@AttributeOverride(name="forVeryHighComplexity",column=@Column(name="TEST_DESIGN_PRODUCTIVITY_FOR_VERY_HIGH_COMPLEXITY"))})
	private ValueByComplexity testDesignProductivity;
	@AttributeOverrides({
		@AttributeOverride(name="forVeryLowComplexity",column=@Column(name="TEST_EXECUTION_PRODUCTIVITY_FOR_VERY_LOW_COMPLEXITY")),
		@AttributeOverride(name="forLowComplexity",column=@Column(name="TEST_EXECUTION_PRODUCTIVITY_FOR_LOW_COMPLEXITY")),
		@AttributeOverride(name="forMediumComplexity",column=@Column(name="TEST_EXECUTION_PRODUCTIVITY_FOR_MEDIUM_COMPLEXITY")),
		@AttributeOverride(name="forHighComplexity",column=@Column(name="TEST_EXECUTION_PRODUCTIVITY_FOR_HIGH_COMPLEXITY")),
		@AttributeOverride(name="forVeryHighComplexity",column=@Column(name="TEST_EXECUTION_PRODUCTIVITY_FOR_VERY_HIGH_COMPLEXITY"))})
	private ValueByComplexity testExecutionProductivity;
	private double testConfigurationComplexityPercentage;
	private double testDataComplexityPercentage;
	private double testTransactionComplexityPercentage;
	private double testValidationComplexityPercentage;
}
