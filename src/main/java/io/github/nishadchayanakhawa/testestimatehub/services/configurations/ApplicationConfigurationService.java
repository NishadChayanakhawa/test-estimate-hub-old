package io.github.nishadchayanakhawa.testestimatehub.services.configurations;

//import section
//logger
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
//java utils
import java.util.Set;
import java.util.stream.Collectors;
//model mapper
import org.modelmapper.ModelMapper;
//spring libraries
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionSystemException;
//spring data validation
import org.springframework.dao.DataIntegrityViolationException;
//entity and DTO object
import io.github.nishadchayanakhawa.testestimatehub.model.configurations.ApplicationConfiguration;
import io.github.nishadchayanakhawa.testestimatehub.model.dto.configurations.ApplicationConfigurationDTO;
//jpa repository
import io.github.nishadchayanakhawa.testestimatehub.repositories.configurations.ApplicationConfigurationRepository;
import io.github.nishadchayanakhawa.testestimatehub.services.configurations.exceptions.ApplicationConfigurationTransactionException;
//exceptions
import io.github.nishadchayanakhawa.testestimatehub.services.configurations.exceptions.DuplicateApplicationConfigurationException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;

/**
 * <b>Class Name</b>: ApplicationConfigurationService<br>
 * <b>Description</b>: Service for processing Application Configuration.<br>
 * @author nishad.chayanakhawa
 */
@Service
public class ApplicationConfigurationService {
	//logger
	private static Logger logger = LoggerFactory.getLogger(ApplicationConfigurationService.class);

	//autowired application configuration repository
	@Autowired
	private ApplicationConfigurationRepository applicationConfigurationRepository;

	//autowired model mapper
	@Autowired
	private ModelMapper modelMapper;

	/**
	 * <b>Method Name</b>: save<br>
	 * <b>Description</b>: Save application configuration.<br>
	 * @param applicationConfigurationToSaveDTO
	 * @return
	 */
	public ApplicationConfigurationDTO save(ApplicationConfigurationDTO applicationConfigurationToSaveDTO) {
		logger.debug("Saving: {}", applicationConfigurationToSaveDTO);
		try {
			//persist application configuration
			ApplicationConfigurationDTO applicationConfigurationSavedDTO = modelMapper.map(
					this.applicationConfigurationRepository
							.save(modelMapper.map(applicationConfigurationToSaveDTO, ApplicationConfiguration.class)),
					ApplicationConfigurationDTO.class);
			logger.debug("Saved: {}", applicationConfigurationToSaveDTO);
			//return persisted application configuration
			return applicationConfigurationSavedDTO;
		} catch (DataIntegrityViolationException e) {
			//throw exception when app-module-subModule combination is not unique
			throw (DuplicateApplicationConfigurationException)
				new DuplicateApplicationConfigurationException(
					String.format("Application configuration for '%s-%s-%s' already exists.",
							applicationConfigurationToSaveDTO.getApplication(),
							applicationConfigurationToSaveDTO.getModule(),
							applicationConfigurationToSaveDTO.getSubModule()))
					.initCause(e);
		} catch(TransactionSystemException e) {
			//retreive constraint violation messages
			ConstraintViolationException re=(ConstraintViolationException)e.getRootCause();
			Set<ConstraintViolation<?>> violations =re.getConstraintViolations();
			String messages=violations.stream()
				.map(ConstraintViolation::getMessage)
				.collect(Collectors.joining(";"));
			//throw exception in case of constraint violation
			throw (ApplicationConfigurationTransactionException)new ApplicationConfigurationTransactionException(messages)
				.initCause(e);
		}
	}
}
