package io.github.nishadchayanakhawa.testestimatehub.model;

//java utils
import java.util.Set;
import java.util.HashSet;

import jakarta.persistence.CascadeType;
//jpa
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
//jpa validations
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
//lombok
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * <b>Class Name</b>: UseCase<br>
 * <b>Description</b>: Use case entity.<br>
 * 
 * @author nishad.chayanakhawa
 */
@Entity
@Table(name = "TEH_USE_CASE")
@Getter
@Setter
@NoArgsConstructor
public class UseCase {
	// requirement
	@Column(name = "OWNER_REQUIREMENT_ID")
	private Long requirementId;

	// id
	@Id
	@Column(name = "USE_CASE_ID")
	@GeneratedValue
	private Long id;

	// summary
	@Column(nullable = false, length = 35)
	@NotBlank(message = "Summary is required")
	@Size(max = 35, message = "{maxLength35}")
	private String summary;

	// corresponding business functionality
	@ManyToOne
	@JoinColumn(name = "APPLICATION_CONFIGURATION_ID", nullable = false)
	private ApplicationConfiguration businessFunctionality;

	// data variation count
	@Min(value = 1, message = "Data variation count cannot be less than 1.")
	private int dataVariationCount;

	// test configuration complexity
	@Column(nullable = false)
	@NotNull(message = "Test configuration complexity is required")
	@Enumerated(EnumType.ORDINAL)
	private Complexity testConfigurationComplexity;

	// test data complexity
	@Column(nullable = false)
	@NotNull(message = "Test data setup complexity is required")
	@Enumerated(EnumType.ORDINAL)
	private Complexity testDataSetupComplexity;

	// test transaction complexity
	@Column(nullable = false)
	@NotNull(message = "Test transaction complexity is required")
	@Enumerated(EnumType.ORDINAL)
	private Complexity testTransactionComplexity;

	// test validation complexity
	@Column(nullable = false)
	@NotNull(message = "Test validation complexity is required")
	@Enumerated(EnumType.ORDINAL)
	private Complexity testValidationComplexity;

	// test types
	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "TEH_USE_CASE_APPLICABLE_TEST_TYPE", joinColumns = {
			@JoinColumn(name = "USE_CASE_ID") }, inverseJoinColumns = @JoinColumn(name = "TEST_TYPE_ID"))
	private Set<TestType> applicableTestTypes = new HashSet<>();
	
	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
	@JoinColumn(name = "OWNER_USE_CASE_ID", referencedColumnName = "USE_CASE_ID")
	private Set<EstimationDetail> estimations=new HashSet<>();
}
