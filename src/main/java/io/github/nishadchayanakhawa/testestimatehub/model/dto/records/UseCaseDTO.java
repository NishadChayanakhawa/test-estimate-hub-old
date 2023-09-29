package io.github.nishadchayanakhawa.testestimatehub.model.dto.records;

//import section
//java util
import java.util.Set;
//configuration dto
import io.github.nishadchayanakhawa.testestimatehub.model.dto.configurations.TestTypeDTO;
//lombok
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;
import lombok.ToString;

/**
 * <b>Class Name</b>: UseCaseDTO<br>
 * <b>Description</b>: Use case entity dto.<br>
 * @author nishad.chayanakhawa
 */
@ToString
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@RequiredArgsConstructor
public class UseCaseDTO {
	//requirement id
	private Long requirementId;
	//use case id
	private Long id;
	
	//summary
	@NonNull
	private String summary;
	
	//business application, module and sub-module
	@NonNull
	private Long businessFunctionalityId;
	
	//data variation count
	@NonNull
	private Integer dataVariationCount;

	//test configuration complexity code and display value
	@NonNull
	private String testConfigurationComplexityCode;
	private String testConfigurationComplexityDisplayValue;
	
	//test data setup complexity code and display value
	@NonNull
	private String testDataSetupComplexityCode;
	private String testDataSetupComplexityDisplayValue;
	
	//test transaction complexity code and display value
	@NonNull
	private String testTransactionComplexityCode;
	private String testTransactionComplexityDisplayValue;
	
	//test validation complexity code and display value
	@NonNull
	private String testValidationComplexityCode;
	private String testValidationComplexityDisplayValue;
	
	//applicable test types
	@NonNull
	private Set<TestTypeDTO> applicableTestTypes;
}
