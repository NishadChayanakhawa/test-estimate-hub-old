package io.github.nishadchayanakhawa.testestimatehub.configurations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import io.github.nishadchayanakhawa.testestimatehub.services.configurations.ApplicationConfigurationService;
import io.github.nishadchayanakhawa.testestimatehub.services.configurations.TestTypeService;
import io.github.nishadchayanakhawa.testestimatehub.services.configurations.ChangeTypeService;
import io.github.nishadchayanakhawa.testestimatehub.model.dto.configurations.ApplicationConfigurationDTO;
import io.github.nishadchayanakhawa.testestimatehub.model.dto.configurations.TestTypeDTO;
import io.github.nishadchayanakhawa.testestimatehub.model.dto.configurations.ChangeTypeDTO;

@Component
public class CommandLineAppStartupRunner implements CommandLineRunner {
	private static final Logger logger=LoggerFactory.getLogger(CommandLineAppStartupRunner.class);

	@Value("${server.port}")
	private int serverPort;
	
	@Autowired
	private ApplicationConfigurationService applicationConfigurationService;
	
	@Autowired
	private TestTypeService testTypeService;
	
	@Autowired
	private ChangeTypeService changeTypeService;
	
	@Override
	public void run(String... args) throws Exception {
		CommandLineAppStartupRunner.logger.info("Application started!!!");
		CommandLineAppStartupRunner.logger.info("Navigate to http://localhost:{}",serverPort);
		
		this.loadApplicationConfiguration();
		this.loadTestType();
		this.loadChangeType();
	}
	
	private void loadApplicationConfiguration() {
		ApplicationConfigurationDTO applicationConfigurationDTO = new ApplicationConfigurationDTO(null,
				"Application1", "Module1", "Sub-Module1", 6, "LOW", null);
		ApplicationConfigurationDTO savedApplicationConfigurationDTO = applicationConfigurationService
				.save(applicationConfigurationDTO);
		logger.info("App Configuration: {}",savedApplicationConfigurationDTO);
	}
	
	private void loadTestType() {
		if(this.testTypeService.getAll().isEmpty()) {
			Arrays.asList(
					new TestTypeDTO(null,"System Integration Testing",100d,20d,20d),
					new TestTypeDTO(null,"Regression Testing",21d,10d,10d),
					new TestTypeDTO(null,"User Acceptance Testing",10d,5d,5d)).stream()
				.forEach(testTypeDTO -> {
					TestTypeDTO testTypeSavedDTO=this.testTypeService.save(testTypeDTO);
					logger.info("Test Type Loaded: {}",testTypeSavedDTO);
				});
		}
	}
	
	private void loadChangeType() {
		if(this.changeTypeService.getAll().isEmpty()) {
			Arrays.asList(
					new ChangeTypeDTO(null,"Significant",1.2d,20d,20d,10d),
					new ChangeTypeDTO(null,"Major",1.0d,15d,15d,10d),
					new ChangeTypeDTO(null,"Minor",0.8d,0d,10d,5d),
					new ChangeTypeDTO(null,"Incident",0.6d,0d,5d,5d)).stream()
				.forEach(changeTypeDTO -> {
					ChangeTypeDTO changeTypeSavedDTO=this.changeTypeService.save(changeTypeDTO);
					logger.info("Change Type Loaded: {}",changeTypeSavedDTO);
				});
		}
	}
}