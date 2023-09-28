package io.github.nishadchayanakhawa.testestimatehub.model.dto.records;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import java.util.List;
import java.util.Set;
import io.github.nishadchayanakhawa.testestimatehub.model.dto.configurations.ApplicationConfigurationDTO;

@ToString
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ChangeDTO {
	private Long id;
	private Long releaseId;
	private String releaseIdentifier;
	private String identifier;
	private String summary;
	private Long changeTypeId;
	private String changeTypeName;
	private LocalDate startDate;
	private LocalDate endDate;
	List<RequirementDTO> requirements;
	Set<ApplicationConfigurationDTO> impactedArea;
}
