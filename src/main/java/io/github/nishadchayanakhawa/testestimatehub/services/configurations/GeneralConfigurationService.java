package io.github.nishadchayanakhawa.testestimatehub.services.configurations;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.github.nishadchayanakhawa.testestimatehub.model.configurations.GeneralConfiguration;
import io.github.nishadchayanakhawa.testestimatehub.model.dto.configurations.GeneralConfigurationDTO;
import io.github.nishadchayanakhawa.testestimatehub.repositories.configurations.GeneralConfigurationRepository;

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
		generalConfiguration.setId(1L);
		GeneralConfigurationDTO savedGeneralConfiguration = modelMapper.map(
				this.generalConfigurationRepository
						.save(modelMapper.map(generalConfiguration, GeneralConfiguration.class)),
				GeneralConfigurationDTO.class);
		logger.info("Saved general configuraion: {}", savedGeneralConfiguration);
		return savedGeneralConfiguration;
	}

	public GeneralConfigurationDTO get() {
		GeneralConfigurationDTO generalConfiguration = modelMapper
				.map(this.generalConfigurationRepository.findById(1L).orElse(new GeneralConfiguration()), GeneralConfigurationDTO.class);
		logger.info("General configuraion found: {}", generalConfiguration);
		return generalConfiguration;
	}
}
