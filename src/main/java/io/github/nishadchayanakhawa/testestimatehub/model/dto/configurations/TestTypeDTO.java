package io.github.nishadchayanakhawa.testestimatehub.model.dto.configurations;

//import section
//lombok
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * <b>Class Name</b>: TestTypeDTO<br>
 * <b>Description</b>: DTO representation of Test Type configuration record.<br>
 * 
 * @author nishad.chayanakhawa
 */
@ToString
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TestTypeDTO {
	// id
	private Long id;
	// test type name
	private String name;
	// relative test case count percentage
	private double relativeTestCaseCountPercentage;
	// Re-execution percentage
	private double reExecutionPercentage;
	// additional cycle execution percentage
	private double additionalCycleExecutionPercentage;

	public TestTypeDTO(Long id) {
		this(id, null, 0d, 0d, 0d);
	}

	public TestTypeDTO(String name, double relativeTestCaseCountPercentage, double reExecutionPercentage,
			double additionalCycleExecutionPercentage) {
		this(null, name, relativeTestCaseCountPercentage, reExecutionPercentage, additionalCycleExecutionPercentage);
	}
}
