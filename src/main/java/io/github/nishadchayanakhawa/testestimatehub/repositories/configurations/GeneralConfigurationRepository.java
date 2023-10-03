package io.github.nishadchayanakhawa.testestimatehub.repositories.configurations;

//import section
//jpa
import org.springframework.data.jpa.repository.JpaRepository;
//entity
import io.github.nishadchayanakhawa.testestimatehub.model.configurations.GeneralConfiguration;

/**
 * <b>Class Name</b>: GeneralConfigurationRepository<br>
 * <b>Description</b>: General configuration jpa repository interface<br>
 * @author nishad.chayanakhawa
 */
public interface GeneralConfigurationRepository extends JpaRepository<GeneralConfiguration,Long>{
	//no need to declare functions as JPA auto-generates.
}
