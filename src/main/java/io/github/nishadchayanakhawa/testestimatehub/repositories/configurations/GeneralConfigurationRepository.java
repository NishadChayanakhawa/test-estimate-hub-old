package io.github.nishadchayanakhawa.testestimatehub.repositories.configurations;

import org.springframework.data.jpa.repository.JpaRepository;

import io.github.nishadchayanakhawa.testestimatehub.model.configurations.GeneralConfiguration;

public interface GeneralConfigurationRepository extends JpaRepository<GeneralConfiguration,Long>{

}
