package io.github.nishadchayanakhawa.testestimatehub.model.records;

import io.github.nishadchayanakhawa.testestimatehub.model.configurations.Complexity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "TEH_REQUIREMENT", uniqueConstraints = {
		@UniqueConstraint(name = "TEH_UNIQUE_REQUIREMENT_PER_CHANGE", columnNames = { "OWNER_CHANGE_ID", "IDENTIFIER" }) })
@Getter
@Setter
@NoArgsConstructor
public class Requirement {
	@Column(name = "OWNER_CHANGE_ID")
	private Long changeId;

	// id
	@Id
	@GeneratedValue
	private Long id;

	@Column(nullable = false, length = 15)
	@NotBlank(message = "Identifier is required")
	@Size(max = 15, message = "{maxLength15}")
	private String identifier;

	@Column(nullable = false, length = 35)
	@NotBlank(message = "Summary is required")
	@Size(max = 35, message = "{maxLength35}")
	private String summary;

	// complexity
	@Column(nullable = false)
	@NotNull(message = "Complexity is required")
	@Enumerated(EnumType.ORDINAL)
	private Complexity complexity;
}
