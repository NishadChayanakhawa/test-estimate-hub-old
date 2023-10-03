package io.github.nishadchayanakhawa.testestimatehub.model.configurations;

import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
public class ValueByComplexity {
	private double forVeryLowComplexity;
	private double forLowComplexity;
	private double forMediumComplexity;
	private double forHighComplexity;
	private double forVeryHighComplexity;
}
