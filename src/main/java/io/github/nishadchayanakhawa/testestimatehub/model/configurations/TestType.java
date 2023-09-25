package io.github.nishadchayanakhawa.testestimatehub.model.configurations;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "TEH_TEST_TYPE",
	uniqueConstraints = { @UniqueConstraint(name = "UNIQUE_TEH_TEST_TYPE_NAME", columnNames = { "NAME" }) })
@Getter
@Setter
@NoArgsConstructor
public class TestType {
	// id
	@Id
	@GeneratedValue
	private Long id;

	@Column(nullable = false, length = 25)
	@NotBlank(message = "Test type name is required")
	@Size(max = 35, message = "{maxLength35}")
	private String name;

	@Column(nullable = false)
	@Min(value = 0, message = "{minPercentage}")
	@Max(value = 100, message = "{maxPercentage}")
	private double relativeTestCaseCountPercentage;

	@Column(nullable = false)
	@Min(value = 0, message = "{minPercentage}")
	@Max(value = 100, message = "{maxPercentage}")
	private double reExecutionPercentage;

	@Column(nullable = false)
	@Min(value = 0, message = "{minPercentage}")
	@Max(value = 100, message = "{maxPercentage}")
	private double additionalCycleExecutionPercentage;
}
