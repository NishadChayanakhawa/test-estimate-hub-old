package io.github.nishadchayanakhawa.testestimatehub.model.dto.records;

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
public class RequirementDTO {
	private String identifier;
	private String summary;
	// complexity code
	private String complexityCode;
	// complexity display value
	private String complexityDisplayValue;
}