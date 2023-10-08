package io.github.nishadchayanakhawa.testestimatehub.controllers.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.github.nishadchayanakhawa.testestimatehub.model.dto.ApplicationConfigurationDTO;
import io.github.nishadchayanakhawa.testestimatehub.services.ApplicationConfigurationService;

@RestController
@RequestMapping("/api/configuration/application")
public class ApplicationConfigurationApi {
	@Autowired
	private ApplicationConfigurationService applicationConfigurationService;
	
	@GetMapping(path="/{id}",produces=MediaType.APPLICATION_JSON_VALUE)
	ResponseEntity<ApplicationConfigurationDTO> get(@PathVariable Long id) {
		return new ResponseEntity<>(this.applicationConfigurationService.get(id),HttpStatus.OK);
	}
}
