package io.github.nishadchayanakhawa.testestimatehub.model.records;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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

	// name
	@Column(nullable = false, length = 25)
	@NotBlank(message = "Release name is required")
	@Size(max = 15, message = "{maxLength15}")
	private String identifier;

	// name
	@Column(nullable = false, length = 25)
	@NotBlank(message = "Release name is required")
	@Size(max = 35, message = "{maxLength35}")
	private String name;
	
	@Column(nullable = false)
	private LocalDate startDate;
	
	@Column(nullable = false)
	private LocalDate endDate;
}
