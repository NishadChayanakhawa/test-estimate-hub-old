package io.github.nishadchayanakhawa.testestimatehub.model.records;

import java.time.LocalDate;

import io.github.nishadchayanakhawa.testestimatehub.model.configurations.ChangeType;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.List;
import java.util.ArrayList;
import java.util.Set;
import java.util.HashSet;
import io.github.nishadchayanakhawa.testestimatehub.model.configurations.ApplicationConfiguration;
@Entity
@Table(name = "TEH_CHANGE", uniqueConstraints = {
		@UniqueConstraint(name = "UNIQUE_TEH_CHANGE_IDENTIFIER", columnNames = { "IDENTIFIER" }) })
@Getter
@Setter
@NoArgsConstructor
public class Change {
	// id
	@Id
	@GeneratedValue
	private Long id;

	@ManyToOne
	@JoinColumn(name = "RELEASE_ID", nullable = false)
	private Release release;

	// identifier
	@Column(nullable = false, length = 15)
	@NotBlank(message = "Change identifier is required")
	@Size(max = 15, message = "{maxLength15}")
	private String identifier;

	// summary
	@Column(nullable = false, length = 35)
	@NotBlank(message = "Change summary is required")
	@Size(max = 35, message = "{maxLength35}")
	private String summary;

	@ManyToOne
	@JoinColumn(name = "CHANGE_TYPE_ID", nullable = false)
	private ChangeType changeType;

	// start date
	@Column(nullable = false)
	private LocalDate startDate;

	// end date
	@Column(nullable = false)
	private LocalDate endDate;

	@ElementCollection(fetch = FetchType.EAGER)
	@CollectionTable(name = "TEH_REQUIREMENT", joinColumns = @JoinColumn(name = "CHANGE_ID"),
			uniqueConstraints= @UniqueConstraint(name="TEH_UNIQUE_REQUIREMENT_PER_CHANGE",
					columnNames= {"CHANGE_ID","IDENTIFIER"}))
	List<Requirement> requirements = new ArrayList<>();
	
	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name="TEH_CHANGE_IMPACT",
			joinColumns=@JoinColumn(name="CHANGE_ID"),
			inverseJoinColumns=@JoinColumn(name="APPLICATION_CONFIGURATION_ID"))
	Set<ApplicationConfiguration> impactedArea=new HashSet<>();
}
