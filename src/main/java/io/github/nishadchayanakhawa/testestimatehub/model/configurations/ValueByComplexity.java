package io.github.nishadchayanakhawa.testestimatehub.model.configurations;

import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
public class ValueByComplexity {
	@Min(value = 1, message = "Productivity value cannot be less than 1.")
	private double forVeryLowComplexity;
	@Min(value = 1, message = "Productivity value cannot be less than 1.")
	private double forLowComplexity;
	@Min(value = 1, message = "Productivity value cannot be less than 1.")
	private double forMediumComplexity;
	@Min(value = 1, message = "Productivity value cannot be less than 1.")
	private double forHighComplexity;
	@Min(value = 1, message = "Productivity value cannot be less than 1.")
	private double forVeryHighComplexity;
}
