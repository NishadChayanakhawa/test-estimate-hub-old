package io.github.nishadchayanakhawa.testestimatehub.model.records;

import io.github.nishadchayanakhawa.testestimatehub.model.configurations.Complexity;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.List;
import java.util.ArrayList;
@Embeddable
public class Requirement {
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
	
//	@ElementCollection(fetch = FetchType.EAGER)
//	@CollectionTable(name = "TEH_USE_CASE", joinColumns = {@JoinColumn(name = "CHANGE_ID"),@JoinColumn(name = "REQUIREMENT_IDENTIFIER")},
//			uniqueConstraints= @UniqueConstraint(name="TEH_UNIQUE_USE_CASE_PER_REQUIREMENT",
//					columnNames= {"CHANGE_ID","REQUIREMENT_IDENTIFIER","IDENTIFIER"}))
//	List<UseCase> useCases=new ArrayList<>();
}
