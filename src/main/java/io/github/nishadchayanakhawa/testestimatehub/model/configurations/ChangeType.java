package io.github.nishadchayanakhawa.testestimatehub.model.configurations;

//import section
//jpa
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
//lombok
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * <b>Class Name</b>: ChangeType<br>
 * <b>Description</b>: Change type configuration record.<br>
 * 
 * @author nishad.chayanakhawa
 */
@Entity
@Table(name = "TEH_CHANGE_TYPE_CONFIGURATION", uniqueConstraints = {
		@UniqueConstraint(name = "UNIQUE_TEH_CHANGE_TYPE_NAME", columnNames = { "NAME" }) })
@Getter
@Setter
@NoArgsConstructor
public class ChangeType {
	// id
	@Id
	@GeneratedValue
	private Long id;

	// name
	@Column(nullable = false, length = 25)
	@NotBlank(message = "Change type name is required")
	@Size(max = 35, message = "{maxLength35}")
	private String name;

	// test case count modifier
	@Column(nullable = false)
	@Min(value = 0, message = "Value cannot be less than 0.")
	private double testCaseCountModifier;

	// test planning effort allocation percentage
	@Column(nullable = false)
	@Min(value = 0, message = "{minPercentage}")
	@Max(value = 100, message = "{maxPercentage}")
	private double testPlanningEffortAllocationPercentage;

	// test preparation effort allocation percentgae
	@Column(nullable = false)
	@Min(value = 0, message = "{minPercentage}")
	@Max(value = 100, message = "{maxPercentage}")
	private double testPreparationEffortAllocationPercentage;

	// management effort allocation percentgae
	@Column(nullable = false)
	@Min(value = 0, message = "{minPercentage}")
	@Max(value = 100, message = "{maxPercentage}")
	private double managementEffortAllocationPercentage;
}
