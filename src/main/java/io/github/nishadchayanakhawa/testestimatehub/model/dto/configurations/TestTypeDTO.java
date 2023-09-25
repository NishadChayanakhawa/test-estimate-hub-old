package io.github.nishadchayanakhawa.testestimatehub.model.dto.configurations;

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
public class TestTypeDTO {
	// id
	private Long id;
	private String name;
	private double relativeTestCaseCountPercentage;
	private double reExecutionPercentage;
	private double additionalCycleExecutionPercentage;
}
