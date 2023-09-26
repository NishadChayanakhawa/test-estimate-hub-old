package io.github.nishadchayanakhawa.testestimatehub.services.configurations;

//import section
//logger
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
//java utils
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
//model mapper
import org.modelmapper.ModelMapper;
//spring
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionSystemException;
//entities and dto
import io.github.nishadchayanakhawa.testestimatehub.model.configurations.ChangeType;
import io.github.nishadchayanakhawa.testestimatehub.model.dto.configurations.ChangeTypeDTO;
//repository
import io.github.nishadchayanakhawa.testestimatehub.repositories.configurations.ChangeTypeRepository;
//exceptions
import io.github.nishadchayanakhawa.testestimatehub.services.configurations.exceptions.DuplicateChangeTypeException;
import io.github.nishadchayanakhawa.testestimatehub.services.configurations.exceptions.ChangeTypeTransactionException;
//validations
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;

/**
 * <b>Class Name</b>: ChangeTypeService<br>
 * <b>Description</b>: Service for processing Change Type.<br>
 * @author nishad.chayanakhawa
 */
@Service
public class ChangeTypeService {
	//logger
	private static final Logger logger = LoggerFactory.getLogger(ChangeTypeService.class);

	//change type repository
	@Autowired
	private ChangeTypeRepository changeTypeRepository;

	//model mapper
	@Autowired
	private ModelMapper modelMapper;

	/**
	 * <b>Method Name</b>: save<br>
	 * <b>Description</b>: Save change type.<br>
	 * @param changeTypeToSaveDTO change type to save as 
	 * {@link io.github.nishadchayanakhawa.testestimatehub.model.dto.configurations.ChangeTypeDTO ChangeTypeDTO}
	 * @return saved change type as 
	 * {@link io.github.nishadchayanakhawa.testestimatehub.model.dto.configurations.ChangeTypeDTO ChangeTypeDTO}
	 */
	public ChangeTypeDTO save(ChangeTypeDTO changeTypeToSaveDTO) {
		logger.debug("Saving Change Type: {}", changeTypeToSaveDTO);
		try {
			//save change type
			ChangeTypeDTO changeTypeSavedDTO = modelMapper.map(
					this.changeTypeRepository.save(modelMapper.map(changeTypeToSaveDTO, ChangeType.class)),
					ChangeTypeDTO.class);
			logger.debug("Saved Change Type: {}", changeTypeSavedDTO);
			//return saved instance
			return changeTypeSavedDTO;
		} catch (DataIntegrityViolationException e) {
			// throw exception when change type name is not unique
			throw (DuplicateChangeTypeException) new DuplicateChangeTypeException(
					String.format("Change type '%s' already exists.", changeTypeToSaveDTO.getName())).initCause(e);
		} catch (TransactionSystemException e) {
			// retreive constraint violation messages
			ConstraintViolationException re = (ConstraintViolationException) e.getRootCause();
			Set<ConstraintViolation<?>> violations = re.getConstraintViolations();
			String messages = violations.stream().map(ConstraintViolation::getMessage).collect(Collectors.joining(";"));
			// throw exception in case of constraint violation
			throw (ChangeTypeTransactionException) new ChangeTypeTransactionException(messages).initCause(e);
		}
	}

	/**
	 * <b>Method Name</b>: getAll<br>
	 * <b>Description</b>: Get list of change types as - 
	 * {@link java.util.List List} of 
	 * {@link io.github.nishadchayanakhawa.testestimatehub.model.dto.configurations.ChangeTypeDTO ChangeTypeDTO}
	 * @return
	 */
	public List<ChangeTypeDTO> getAll() {
		//get list of saved change types
		logger.debug("Retreiving all change type records");
		List<ChangeTypeDTO> changeTypes = this.changeTypeRepository.findAll().stream()
				.map(changeType -> modelMapper.map(changeType, ChangeTypeDTO.class)).toList();
		logger.debug("Change types: {}", changeTypes);
		//return the list
		return changeTypes;
	}
	
	/**
	 * <b>Method Name</b>: get<br>
	 * <b>Description</b>: Get change type as 
	 * {@link io.github.nishadchayanakhawa.testestimatehub.model.dto.configurations.ChangeTypeDTO ChangeTypeDTO} 
	 * based on id
	 * @param id change type id as
	 *  as {@link java.lang.Long Long}
	 * @return matching change type as
	 * {@link io.github.nishadchayanakhawa.testestimatehub.model.dto.configurations.ChangeTypeDTO ChangeTypeDTO}
	 */
	public ChangeTypeDTO get(Long id) {
		//retreive change type based on id
		logger.debug("Retreiving change type for id {}",id);
		ChangeTypeDTO changeTypeDTO=modelMapper.map(this.changeTypeRepository.findById(id).orElseThrow(), ChangeTypeDTO.class);
		logger.debug("Retreived change type: {}",changeTypeDTO);
		//return change type
		return changeTypeDTO;
	}
	
	/**
	 * <b>Method Name</b>: delete<br>
	 * <b>Description</b>: Delete change type.<br>
	 * @param changeTypeToDeleteDTO change type to delete as 
	 * {@link io.github.nishadchayanakhawa.testestimatehub.model.dto.configurations.ChangeTypeDTO ChangeTypeDTO}. 
	 * Only id is required, other properties are ignored and can be null.
	 */
	public void delete(ChangeTypeDTO changeTypeToDeleteDTO) {
		//delete change type record
		logger.debug("Deleting change type: {}",changeTypeToDeleteDTO);
		this.changeTypeRepository.deleteById(changeTypeToDeleteDTO.getId());
		logger.debug("Deleted change type successfully.");
	}
}