package io.github.nishadchayanakhawa.testestimatehub.model.configurations;

//import section
//jpa
import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
//validations
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
//lombok
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * <b>Class Name</b>: GeneralConfiguration<br>
 * <b>Description</b>: General configuration entity.<br>
 * @author nishad.chayanakhawa
 */
@Entity
@Table(name = "TEH_GENERAL_CONFIGURATION")
@Getter
@Setter
@NoArgsConstructor
public class GeneralConfiguration {
	//id
	@Id
	@GeneratedValue
	private Long id;
	
	//test design productivity
	@AttributeOverride(name = "forVeryLowComplexity", column = @Column(name = "TEST_DESIGN_PRODUCTIVITY_FOR_VERY_LOW_COMPLEXITY"))
	@AttributeOverride(name = "forLowComplexity", column = @Column(name = "TEST_DESIGN_PRODUCTIVITY_FOR_LOW_COMPLEXITY"))
	@AttributeOverride(name = "forMediumComplexity", column = @Column(name = "TEST_DESIGN_PRODUCTIVITY_FOR_MEDIUM_COMPLEXITY"))
	@AttributeOverride(name = "forHighComplexity", column = @Column(name = "TEST_DESIGN_PRODUCTIVITY_FOR_HIGH_COMPLEXITY"))
	@AttributeOverride(name = "forVeryHighComplexity", column = @Column(name = "TEST_DESIGN_PRODUCTIVITY_FOR_VERY_HIGH_COMPLEXITY"))
	@Valid
	private ValueByComplexity testDesignProductivity;
	
	//test execution productivity
	@AttributeOverride(name = "forVeryLowComplexity", column = @Column(name = "TEST_EXECUTION_PRODUCTIVITY_FOR_VERY_LOW_COMPLEXITY"))
	@AttributeOverride(name = "forLowComplexity", column = @Column(name = "TEST_EXECUTION_PRODUCTIVITY_FOR_LOW_COMPLEXITY"))
	@AttributeOverride(name = "forMediumComplexity", column = @Column(name = "TEST_EXECUTION_PRODUCTIVITY_FOR_MEDIUM_COMPLEXITY"))
	@AttributeOverride(name = "forHighComplexity", column = @Column(name = "TEST_EXECUTION_PRODUCTIVITY_FOR_HIGH_COMPLEXITY"))
	@AttributeOverride(name = "forVeryHighComplexity", column = @Column(name = "TEST_EXECUTION_PRODUCTIVITY_FOR_VERY_HIGH_COMPLEXITY"))
	@Valid
	private ValueByComplexity testExecutionProductivity;
	
	//test configuration complexity weightage
	@Min(value = 0, message = "Weightage percentage cannot be less than 0.")
	private double testConfigurationComplexityPercentage;
	
	//test data complexity weightage
	@Min(value = 0, message = "Weightage percentage cannot be less than 0.")
	private double testDataComplexityPercentage;
	
	//test transaction complexity weightage
	@Min(value = 0, message = "Weightage percentage cannot be less than 0.")
	private double testTransactionComplexityPercentage;
	
	//test validation complexity weightage
	@Min(value = 0, message = "Weightage percentage cannot be less than 0.")
	private double testValidationComplexityPercentage;
}
