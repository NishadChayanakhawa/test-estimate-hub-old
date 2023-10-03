package io.github.nishadchayanakhawa.testestimatehub.model.configurations;

//import section
//jpa
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.MapKeyColumn;
import jakarta.persistence.Table;
//validations
import jakarta.validation.constraints.Min;
//lombok
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
//java utils
import java.util.Map;
import java.util.EnumMap;

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
	@ElementCollection(fetch=FetchType.EAGER)
	@CollectionTable(name="TEH_TEST_DESIGN_PRODUCTIVITY_CONFIGURATION")
	@MapKeyColumn(name="COMPLEXITY")
	@Column(name="PRODUCTIVITY")
	private Map<Complexity,Double> testDesignProductivity=new EnumMap<>(Complexity.class);
	
	//test execution productivity
	@ElementCollection(fetch=FetchType.EAGER)
	@CollectionTable(name="TEH_TEST_EXECUTION_PRODUCTIVITY_CONFIGURATION")
	@MapKeyColumn(name="COMPLEXITY")
	@Column(name="PRODUCTIVITY")
	private Map<Complexity,Double> testExecutionProductivity=new EnumMap<>(Complexity.class);
	
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
