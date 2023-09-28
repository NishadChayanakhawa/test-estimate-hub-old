package io.github.nishadchayanakhawa.testestimatehub.model.dto.records;

import java.util.Set;
import io.github.nishadchayanakhawa.testestimatehub.model.dto.configurations.TestTypeDTO;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@RequiredArgsConstructor
public class UseCaseDTO {
	private Long requirementId;
	private Long id;
	
	@NonNull
	private String summary;
	@NonNull
	private Long businessFunctionalityId;
	@NonNull
	private Integer dataVariationCount;

	@NonNull
	private String testConfigurationComplexityCode;
	
	private String testConfigurationComplexityDisplayValue;
	
	@NonNull
	private String testDataSetupComplexityCode;
	
	private String testDataSetupComplexityDisplayValue;
	
	@NonNull
	private String testTransactionComplexityCode;
	
	private String testTransactionComplexityDisplayValue;
	
	@NonNull
	private String testValidationComplexityCode;
	
	private String testValidationComplexityDisplayValue;
	
	@NonNull
	private Set<TestTypeDTO> applicableTestTypes;
}
