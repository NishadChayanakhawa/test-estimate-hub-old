package io.github.nishadchayanakhawa.testestimatehub.model.records;

import io.github.nishadchayanakhawa.testestimatehub.model.configurations.TestType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "TEH_ESTIMATION_DETAIL")
@Getter
@Setter
@NoArgsConstructor
public class EstimationDetail {
	@Getter(AccessLevel.NONE)
	@Column(name = "OWNER_USE_CASE_ID")
	private Long useCaseId;

	// id
	@Id
	@Column
	@GeneratedValue
	private Long id;

	// change type
	@ManyToOne
	@JoinColumn(name = "TEST_TYPE_ID", nullable = false)
	private TestType testType;
	
	private int testCaseCount;
	
	private int executionCount;
	private int reExecutionCount;
	private int additionalCycleExecutionCount;
	private int totalExecutionCount;
}
