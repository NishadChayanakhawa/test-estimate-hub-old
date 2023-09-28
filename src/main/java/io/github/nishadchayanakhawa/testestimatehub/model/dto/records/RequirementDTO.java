package io.github.nishadchayanakhawa.testestimatehub.model.dto.records;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.NonNull;

@ToString
@Getter
@Setter
@NoArgsConstructor
@RequiredArgsConstructor
public class RequirementDTO {
	@Setter(AccessLevel.NONE)
	private Long id;
	
	@Setter(AccessLevel.NONE)
	private Long changeId;
	
	@NonNull
	private String identifier;
	
	@NonNull
	private String summary;
	// complexity code
	
	@NonNull
	private String complexityCode;
	// complexity display value
	
	@Setter(AccessLevel.NONE)
	private String complexityDisplayValue;
	
//	List<UseCaseDTO> useCases=new ArrayList<>();
}
