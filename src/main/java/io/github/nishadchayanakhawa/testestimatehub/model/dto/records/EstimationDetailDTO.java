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
public class EstimationDetailDTO {
	private Long useCaseId;
	private Long id;
	
	private String testTypeId;
	private String testTypeName;
}
