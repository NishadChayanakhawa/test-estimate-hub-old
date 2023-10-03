package io.github.nishadchayanakhawa.testestimatehub.model.dto.records;

//import section
//jav util
import java.util.Set;
//lombok
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.NonNull;

/**
 * <b>Class Name</b>: RequirementDTO<br>
 * <b>Description</b>: Requirement entity DTO.<br>
 * 
 * @author nishad.chayanakhawa
 */
@ToString
@Getter
@Setter
@NoArgsConstructor
@RequiredArgsConstructor
public class RequirementDTO {
	// id
	private Long id;

	// change id
	@Setter(AccessLevel.NONE)
	private Long changeId;

	// requirement identifier
	@NonNull
	private String identifier;

	// requirement summary
	@NonNull
	private String summary;
	// complexity code

	// complexity code
	@NonNull
	private String complexityCode;

	// complexity display value
	@Setter(AccessLevel.NONE)
	private String complexityDisplayValue;

	// use cases
	Set<UseCaseDTO> useCases;
}
