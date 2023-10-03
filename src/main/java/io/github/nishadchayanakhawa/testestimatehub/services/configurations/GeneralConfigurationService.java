package io.github.nishadchayanakhawa.testestimatehub.services.configurations;

import java.util.Set;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionSystemException;

import io.github.nishadchayanakhawa.testestimatehub.model.configurations.GeneralConfiguration;
import io.github.nishadchayanakhawa.testestimatehub.model.dto.configurations.GeneralConfigurationDTO;
import io.github.nishadchayanakhawa.testestimatehub.repositories.configurations.GeneralConfigurationRepository;
import io.github.nishadchayanakhawa.testestimatehub.services.configurations.exceptions.GeneralConfigurationTransactionException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;

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

	public GeneralConfigurationDTO save(GeneralConfigurationDTO generalConfiguration) {
		logger.info("Saving general configuraion: {}", generalConfiguration);
		if ((generalConfiguration.getTestConfigurationComplexityPercentage()
				+ generalConfiguration.getTestDataComplexityPercentage()
				+ generalConfiguration.getTestTransactionComplexityPercentage()
				+ generalConfiguration.getTestValidationComplexityPercentage()) != 100) {
			throw new GeneralConfigurationTransactionException(
					"Test complexity weightage percentages must add up to 100.");
		}
		generalConfiguration.setId(1L);
		try {
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

	public GeneralConfigurationDTO get() {
		GeneralConfigurationDTO generalConfiguration = modelMapper.map(
				this.generalConfigurationRepository.findById(1L).orElse(new GeneralConfiguration()),
				GeneralConfigurationDTO.class);
		logger.info("General configuraion found: {}", generalConfiguration);
		return generalConfiguration;
	}
}
