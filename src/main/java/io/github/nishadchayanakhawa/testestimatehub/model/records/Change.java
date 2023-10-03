package io.github.nishadchayanakhawa.testestimatehub.model.records;

//import section
//java time
import java.time.LocalDate;
//configuration entities
import io.github.nishadchayanakhawa.testestimatehub.model.configurations.ChangeType;
import io.github.nishadchayanakhawa.testestimatehub.model.configurations.ApplicationConfiguration;
//jpa
import jakarta.persistence.Column;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
//jpa validations
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
//lombok
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
//java utils
import java.util.Set;
import java.util.HashSet;

/**
 * <b>Class Name</b>: Change<br>
 * <b>Description</b>: Change entity.<br>
 * 
 * @author nishad.chayanakhawa
 */
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
	@Column(name = "CHANGE_ID")
	private Long id;

	// release
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

	// change type
	@ManyToOne
	@JoinColumn(name = "CHANGE_TYPE_ID", nullable = false)
	private ChangeType changeType;

	// start date
	@Column(nullable = false)
	private LocalDate startDate;

	// end date
	@Column(nullable = false)
	private LocalDate endDate;

	// requirements
	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
	@JoinColumn(name = "OWNER_CHANGE_ID", referencedColumnName = "CHANGE_ID")
	Set<Requirement> requirements = new HashSet<>();

	// impacted applications, modules and sub-modules
	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "TEH_CHANGE_IMPACT", 
		joinColumns = @JoinColumn(name = "CHANGE_ID"), 
		inverseJoinColumns = @JoinColumn(name = "APPLICATION_CONFIGURATION_ID"))
	Set<ApplicationConfiguration> impactedArea = new HashSet<>();
	
	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
	@JoinColumn(name = "OWNER_CHANGE_ID", referencedColumnName = "CHANGE_ID")
	private Set<Estimation> estimations=new HashSet<>();
	
	private int totalTestCases;
	private int totalExecutions;
	private double efforts;
	
	private double testPlanningEfforts;
	private double testPreparationEfforts;
	private double managementEfforts;
	
	private double finalEfforts;
}
