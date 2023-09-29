package io.github.nishadchayanakhawa.testestimatehub.model.records;

//import section
//configuration entities
import io.github.nishadchayanakhawa.testestimatehub.model.configurations.Complexity;
//jpa
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
//jpa validations
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
//lombok
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
//java utils
import java.util.Set;
import java.util.HashSet;

/**
 * <b>Class Name</b>: Requirement<br>
 * <b>Description</b>: Requirement entity.<br>
 * 
 * @author nishad.chayanakhawa
 */
@Entity
@Table(name = "TEH_REQUIREMENT", uniqueConstraints = {
		@UniqueConstraint(name = "TEH_UNIQUE_REQUIREMENT_PER_CHANGE", columnNames = { "OWNER_CHANGE_ID",
				"IDENTIFIER" }) })
@Getter
@Setter
@NoArgsConstructor
public class Requirement {
	// change
	@Column(name = "OWNER_CHANGE_ID")
	private Long changeId;

	// id
	@Id
	@GeneratedValue
	@Column(name = "REQUIREMENT_ID")
	private Long id;

	// identifier
	@Column(nullable = false, length = 15)
	@NotBlank(message = "Identifier is required")
	@Size(max = 15, message = "{maxLength15}")
	private String identifier;

	// summary
	@Column(nullable = false, length = 35)
	@NotBlank(message = "Summary is required")
	@Size(max = 35, message = "{maxLength35}")
	private String summary;

	// complexity
	@Column(nullable = false)
	@NotNull(message = "Complexity is required")
	@Enumerated(EnumType.ORDINAL)
	private Complexity complexity;

	// use cases
	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
	@JoinColumn(name = "OWNER_REQUIREMENT_ID", referencedColumnName = "REQUIREMENT_ID")
	Set<UseCase> useCases = new HashSet<>();
}
