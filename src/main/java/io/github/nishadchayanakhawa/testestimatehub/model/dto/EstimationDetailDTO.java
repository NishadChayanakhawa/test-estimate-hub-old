package io.github.nishadchayanakhawa.testestimatehub.model.dto;

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
	private Long changeId;
	private Long id;
	
	private String testTypeId;
	private String testTypeName;
	
	private int testCaseCount;
	
	private int executionCount;
	private int reExecutionCount;
	private int additionalCycleExecutionCount;
	private int totalExecutionCount;
	
	private double designEfforts;
	private double executionEfforts;
	private double totalEfforts;
}
