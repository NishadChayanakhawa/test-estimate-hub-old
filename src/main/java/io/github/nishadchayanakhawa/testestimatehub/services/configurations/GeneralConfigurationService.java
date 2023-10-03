package io.github.nishadchayanakhawa.testestimatehub.services.configurations;

//import section
//java utils
import java.util.Set;
import java.util.stream.Collectors;
//model mapper
import org.modelmapper.ModelMapper;
//logger
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
//spring
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionSystemException;
//entities and repositories
import io.github.nishadchayanakhawa.testestimatehub.model.configurations.GeneralConfiguration;
import io.github.nishadchayanakhawa.testestimatehub.model.dto.configurations.GeneralConfigurationDTO;
import io.github.nishadchayanakhawa.testestimatehub.repositories.configurations.GeneralConfigurationRepository;
//exceptions
import io.github.nishadchayanakhawa.testestimatehub.services.configurations.exceptions.GeneralConfigurationTransactionException;
//validations
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;

/**
 * <b>Class Name</b>: GeneralConfigurationService<br>
 * <b>Description</b>: General configuration entity service.<br>
 * @author nishad.chayanakhawa
 */
@Service
public class GeneralConfigurationService {
	// logger
	private static Logger logger = LoggerFactory.getLogger(GeneralConfigurationService.class);

	// autowired application configuration repository
	@Autowired
	private GeneralConfigurationRepository generalConfigurationRepository;

	// autowired model mapper
	@Autowired
	private ModelMapper modelMapper;

	/**
	 * <b>Method Name</b>: save<br>
	 * <b>Description</b>: Save general configuration.<br>
	 * @param generalConfiguration configuration to save as 
	 * {@link io.github.nishadchayanakhawa.testestimatehub.model.dto.configurations.GeneralConfigurationDTO GeneralConfigurationDTO}
	 * @return saved configuration as 
	 * {@link io.github.nishadchayanakhawa.testestimatehub.model.dto.configurations.GeneralConfigurationDTO GeneralConfigurationDTO}
	 */
	public GeneralConfigurationDTO save(GeneralConfigurationDTO generalConfiguration) {
		logger.info("Saving general configuraion: {}", generalConfiguration);
		//validate if weightage sums up to exactly 100
		if ((generalConfiguration.getTestConfigurationComplexityPercentage()
				+ generalConfiguration.getTestDataComplexityPercentage()
				+ generalConfiguration.getTestTransactionComplexityPercentage()
				+ generalConfiguration.getTestValidationComplexityPercentage()) != 100) {
			//if validation fails, throw an exception
			throw new GeneralConfigurationTransactionException(
					"Test complexity weightage percentages must add up to 100.");
		}
		//override id to 1, as only single record will exist throught lifecycle.
		generalConfiguration.setId(1L);
		try {
			//persist general configuration
			GeneralConfigurationDTO savedGeneralConfiguration = modelMapper.map(
					this.generalConfigurationRepository
							.save(modelMapper.map(generalConfiguration, GeneralConfiguration.class)),
					GeneralConfigurationDTO.class);
			logger.info("Saved general configuraion: {}", savedGeneralConfiguration);
			return savedGeneralConfiguration;
		} catch (TransactionSystemException e) {
			// retreive constraint violation messages
			ConstraintViolationException re = (ConstraintViolationException) e.getRootCause();
			Set<ConstraintViolation<?>> violations = re.getConstraintViolations();
			String messages = violations.stream().map(ConstraintViolation::getMessage).collect(Collectors.joining(";"));
			// throw exception in case of constraint violation
			throw (GeneralConfigurationTransactionException) new GeneralConfigurationTransactionException(messages).initCause(e);
		}
	}

	/**
	 * <b>Method Name</b>: get<br>
	 * <b>Description</b>: Retreive generalconfiguration record. ID looked up is '1'.<br>
	 * @return general configuration record as 
	 * {@link io.github.nishadchayanakhawa.testestimatehub.model.dto.configurations.GeneralConfigurationDTO GeneralConfigurationDTO}
	 */
	public GeneralConfigurationDTO get() {
		GeneralConfigurationDTO generalConfiguration = modelMapper.map(
				this.generalConfigurationRepository.findById(1L).orElse(new GeneralConfiguration()),
				GeneralConfigurationDTO.class);
		logger.info("General configuraion found: {}", generalConfiguration);
		return generalConfiguration;
	}
}
