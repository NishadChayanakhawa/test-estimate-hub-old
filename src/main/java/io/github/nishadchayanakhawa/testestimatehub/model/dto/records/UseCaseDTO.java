package io.github.nishadchayanakhawa.testestimatehub.model.dto.records;

import java.util.HashSet;
import java.util.Set;

import io.github.nishadchayanakhawa.testestimatehub.model.dto.configurations.TestTypeDTO;
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
public class UseCaseDTO {
	private String summary;
	private Long businessFunctionalityId;
	private int dataVariationCount;

	private String testConfigurationComplexityCode;
	private String testConfigurationComplexityDisplayValue;
	private String testDataSetupComplexityCode;
	private String testDataSetupComplexityDisplayValue;
	private String testTransactionComplexityCode;
	private String testTransactionComplexityDisplayValue;
	private String testValidationComplexityCode;
	private String testValidationComplexityDisplayValue;
	
	private Set<TestTypeDTO> applicableTestTypes = new HashSet<>();
}
