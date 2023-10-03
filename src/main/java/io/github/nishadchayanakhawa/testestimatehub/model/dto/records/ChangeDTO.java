package io.github.nishadchayanakhawa.testestimatehub.model.dto.records;

//import section
//java time
import java.time.LocalDate;
//lombok
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
//java util
import java.util.Set;
//configuration DTO
import io.github.nishadchayanakhawa.testestimatehub.model.dto.configurations.ApplicationConfigurationDTO;

/**
 * <b>Class Name</b>: ChangeDTO<br>
 * <b>Description</b>: Change entity DTO.<br>
 * 
 * @author nishad.chayanakhawa
 */
@ToString
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ChangeDTO {
	// id
	private Long id;
	// release id
	private Long releaseId;
	// release identifier
	private String releaseIdentifier;
	// change identifier
	private String identifier;
	// change summary
	private String summary;
	// change type id
	private Long changeTypeId;
	// change type name
	private String changeTypeName;
	// start date
	private LocalDate startDate;
	// end date
	private LocalDate endDate;
	// requirements
	private Set<RequirementDTO> requirements;
	// impacted areas
	private Set<ApplicationConfigurationDTO> impactedArea;
	
	private Set<EstimationDTO> estimations;
	
	private int totalTestCases;
	private int totalExecutions;
	private double efforts;
	
	private double testPlanningEfforts;
	private double testPreparationEfforts;
	private double managementEfforts;
	
	private double finalEfforts;
}
