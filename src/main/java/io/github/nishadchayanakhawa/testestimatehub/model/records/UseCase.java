package io.github.nishadchayanakhawa.testestimatehub.model.records;

import io.github.nishadchayanakhawa.testestimatehub.model.configurations.ApplicationConfiguration;
import io.github.nishadchayanakhawa.testestimatehub.model.configurations.Complexity;
import io.github.nishadchayanakhawa.testestimatehub.model.configurations.TestType;
import java.util.Set;
import java.util.HashSet;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Embeddable
public class UseCase {
	@Column(nullable = false, length = 15)
	@NotBlank(message = "Identifier is required")
	@Size(max = 15, message = "{maxLength15}")
	private String identifier;
	
	@Column(nullable = false, length = 35)
	@NotBlank(message = "Summary is required")
	@Size(max = 35, message = "{maxLength35}")
	private String summary;

	@ManyToOne
	@JoinColumn(name = "APPLICATION_CONFIGURATION_ID", nullable = false)
	private ApplicationConfiguration businessFunctionality;

	@Min(value = 1, message = "Data variation count cannot be less than 1.")
	private int dataVariationCount;

	// complexity
	@Column(nullable = false)
	@NotNull(message = "Test configuration complexity is required")
	@Enumerated(EnumType.ORDINAL)
	private Complexity testConfigurationComplexity;

	// complexity
	@Column(nullable = false)
	@NotNull(message = "Test data setup complexity is required")
	@Enumerated(EnumType.ORDINAL)
	private Complexity testDataSetupComplexity;

	// complexity
	@Column(nullable = false)
	@NotNull(message = "Test transaction complexity is required")
	@Enumerated(EnumType.ORDINAL)
	private Complexity testTransactionComplexity;

	// complexity
	@Column(nullable = false)
	@NotNull(message = "Test validation complexity is required")
	@Enumerated(EnumType.ORDINAL)
	private Complexity testValidationComplexity;

	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "TEH_USE_CASE_APPLICABLE_TEST_TYPE",
		joinColumns = {@JoinColumn(name = "CHANGE_ID"),@JoinColumn(name = "REQUIREMENT_IDENTIFIER"),@JoinColumn(name = "USE_CASE_IDENTIFIER")},
		inverseJoinColumns = @JoinColumn(name = "TEST_TYPE_ID"))
	private Set<TestType> applicableTestTypes = new HashSet<>();
}
