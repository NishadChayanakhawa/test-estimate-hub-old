package io.github.nishadchayanakhawa.testestimatehub.services.records;

//import section
//java-util
import java.util.List;
//model mapper
import org.modelmapper.ModelMapper;
//logger
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
//spring
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
//repositories, entities and exceptions
import io.github.nishadchayanakhawa.testestimatehub.repositories.records.ChangeRepository;
import io.github.nishadchayanakhawa.testestimatehub.repositories.records.RequirementRepository;
import io.github.nishadchayanakhawa.testestimatehub.services.records.exceptions.DuplicateChangeException;
import io.github.nishadchayanakhawa.testestimatehub.services.records.exceptions.DuplicateChangeImpactException;
import io.github.nishadchayanakhawa.testestimatehub.services.records.exceptions.DuplicateRequirementException;
import io.github.nishadchayanakhawa.testestimatehub.model.dto.records.ChangeDTO;
import io.github.nishadchayanakhawa.testestimatehub.model.dto.records.RequirementDTO;
import io.github.nishadchayanakhawa.testestimatehub.model.records.Change;
import io.github.nishadchayanakhawa.testestimatehub.model.records.Requirement;

/**
 * <b>Class Name</b>: ChangeService<br>
 * <b>Description</b>: Service for change entity.<br>
 * 
 * @author nishad.chayanakhawa
 */
@Service
public class ChangeService {
	// logger
	private static final Logger logger = LoggerFactory.getLogger(ChangeService.class);

	// change type repository
	@Autowired
	private ChangeRepository changeRepository;

	// requirement repository
	@Autowired
	private RequirementRepository requirementRepository;

	// model mapper
	@Autowired
	private ModelMapper modelMapper;

	/**
	 * <b>Method Name</b>: save<br>
	 * <b>Description</b>: Save change record.<br>
	 * 
	 * @param changeToSaveDTO change record to save as
	 *                        {@link io.github.nishadchayanakhawa.testestimatehub.model.dto.records.ChangeDTO
	 *                        ChangeDTO}
	 * @return saved change instance as
	 *         {@link io.github.nishadchayanakhawa.testestimatehub.model.dto.records.ChangeDTO
	 *         ChangeDTO}
	 */
	public ChangeDTO save(ChangeDTO changeToSaveDTO) {
		logger.debug("Change to save: {}", changeToSaveDTO);
		try {
			// save change record
			ChangeDTO savedChangeDTO = modelMapper
					.map(this.changeRepository.save(modelMapper.map(changeToSaveDTO, Change.class)), ChangeDTO.class);
			logger.debug("Saved change : {}", savedChangeDTO);
			// return saved change record
			return savedChangeDTO;
		} catch (DataIntegrityViolationException e) {
			if (e.getMessage().contains("TEH_UNIQUE_REQUIREMENT_PER_CHANGE")) {
				// throw exception when requirement identifier is not unique
				throw (DuplicateRequirementException) new DuplicateRequirementException(
						"Please review identifiers used for requirements. Single change record cannot have duplicate identifiers.")
						.initCause(e);
			} else if (e.getMessage().contains("PUBLIC.PRIMARY_KEY_6E ON PUBLIC.TEH_CHANGE_IMPACT")) {
				// throw exception when app configuration identifier is not unique
				throw (DuplicateChangeImpactException) new DuplicateChangeImpactException(
						"Please remove duplicate entries from impacted area.").initCause(e);
			} else {
				// throw exception when change identifier is not unique
				logger.error("[][]: {}", e.getMessage());
				throw (DuplicateChangeException) new DuplicateChangeException(
						String.format("Change '%s' already exists.", changeToSaveDTO.getIdentifier())).initCause(e);
			}
		}
	}

	/**
	 * <b>Method Name</b>: getAll<br>
	 * <b>Description</b>: Get list of all saved changes.<br>
	 * 
	 * @return {@link java.util.List List} of
	 *         {@link io.github.nishadchayanakhawa.testestimatehub.model.dto.records.ChangeDTO
	 *         ChangeDTO}
	 */
	public List<ChangeDTO> getAll() {
		// get list of saved change records
		logger.debug("Retreiving all change records");
		List<ChangeDTO> changes = this.changeRepository.findAll().stream()
				.map(change -> modelMapper.map(change, ChangeDTO.class)).toList();
		logger.debug("Changes: {}", changes);
		// return the list
		return changes;
	}

	/**
	 * <b>Method Name</b>: get<br>
	 * <b>Description</b>: Retreive change record based on id.<br>
	 * 
	 * @param id for change record as {@link java.lang.Long Long}
	 * @return matching change record as
	 *         {@link io.github.nishadchayanakhawa.testestimatehub.model.dto.records.ChangeDTO
	 *         ChangeDTO}
	 */
	public ChangeDTO get(Long id) {
		// retreive change based on id
		logger.debug("Retreiving change for id {}", id);
		ChangeDTO changeDTO = modelMapper.map(this.changeRepository.findById(id).orElseThrow(), ChangeDTO.class);
		logger.debug("Retreived change: {}", changeDTO);
		// return change
		return changeDTO;
	}

	/**
	 * <b>Method Name</b>: delete<br>
	 * <b>Description</b>: Delete change record<br>
	 * 
	 * @param changeToDeleteDTO change record to delete as
	 *                          {@link io.github.nishadchayanakhawa.testestimatehub.model.dto.records.ChangeDTO
	 *                          ChangeDTO}. Only id is required, other fields are
	 *                          ignored and hence can be set to null.
	 */
	public void delete(ChangeDTO changeToDeleteDTO) {
		// delete change record
		logger.debug("Deleting change: {}", changeToDeleteDTO);
		this.changeRepository.deleteById(changeToDeleteDTO.getId());
		logger.debug("Deleted change successfully.");
	}

	public RequirementDTO getRequirement(Long id) {
		logger.debug("Looking up requirement with id: {}", id);
		RequirementDTO requirement = modelMapper.map(this.requirementRepository.findById(id).orElseThrow(),
				RequirementDTO.class);
		logger.debug("Requirement located: {}", requirement);
		return requirement;
	}

	public RequirementDTO saveUseCases(RequirementDTO requirementWithUseCasesToSave) {
		logger.debug("Saving use cases within requirement: {}", requirementWithUseCasesToSave);
		RequirementDTO originalRequirement = this.getRequirement(requirementWithUseCasesToSave.getId());
		originalRequirement.setUseCases(requirementWithUseCasesToSave.getUseCases());
		
		originalRequirement.getUseCases().stream().forEach(useCase -> {
			if(useCase.getRequirementId()==null) {
				useCase.setRequirementId(originalRequirement.getId());
			}
		});

		RequirementDTO savedRequirementWithUseCases = modelMapper.map(
				this.requirementRepository.save(modelMapper.map(originalRequirement, Requirement.class)),
				RequirementDTO.class);
		return savedRequirementWithUseCases;
	}
}