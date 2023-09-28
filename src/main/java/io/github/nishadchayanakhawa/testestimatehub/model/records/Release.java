package io.github.nishadchayanakhawa.testestimatehub.model.records;

//import section
//java time
import java.time.LocalDate;
//jpa
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
//validation
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
//lombok
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * <b>Class Name</b>: Release<br>
 * <b>Description</b>: Release record entity.<br>
 * @author nishad.chayanakhawa
 */
@Entity
@Table(name = "TEH_RELEASE", uniqueConstraints = {
		@UniqueConstraint(name = "UNIQUE_TEH_RELEASE_IDENTIFIER", columnNames = { "IDENTIFIER" }) })
@Getter
@Setter
@NoArgsConstructor
public class Release {
	// id
	@Id
	@GeneratedValue
	private Long id;

	// identifier
	@Column(nullable = false, length = 15)
	@NotBlank(message = "Release identifier is required")
	@Size(max = 15, message = "{maxLength15}")
	private String identifier;

	// name
	@Column(nullable = false, length = 35)
	@NotBlank(message = "Release name is required")
	@Size(max = 35, message = "{maxLength35}")
	private String name;
	
	//start date
	@Column(nullable = false)
	private LocalDate startDate;
	
	//end date
	@Column(nullable = false)
	private LocalDate endDate;
}
