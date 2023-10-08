package io.github.nishadchayanakhawa.testestimatehub.model.dto;

//import section
//lombok
import lombok.Getter;
import lombok.Setter;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * <b>Class Name</b>: ApplicationConfigurationDTO<br>
 * <b>Description</b>: DTO representation of Application Configuration.<br>
 * 
 * @author nishad.chayanakhawa
 */
@ToString
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ApplicationConfigurationDTO {
	// id
	private Long id;
	// application name
	private String application;
	// module name
	private String module;
	// sub-module name
	private String subModule;
	// base test script count
	private int baseTestScriptCount;
	// complexity code
	private String complexityCode;
	// complexity display value
	private String complexityDisplayValue;

	public ApplicationConfigurationDTO(Long id) {
		this(id, null, null, null, 0, null, null);
	}

	public ApplicationConfigurationDTO(String application, String module, String subModule, int baseTestScriptCount,
			String complexityCode) {
		this(null, application, module, subModule, baseTestScriptCount, complexityCode, null);
	}
}
