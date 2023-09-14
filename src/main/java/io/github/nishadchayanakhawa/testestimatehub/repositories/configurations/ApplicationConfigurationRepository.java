package io.github.nishadchayanakhawa.testestimatehub.repositories.configurations;

//import section
//spring data library
import org.springframework.data.jpa.repository.JpaRepository;
//entity
import io.github.nishadchayanakhawa.testestimatehub.model.configurations.ApplicationConfiguration;

/**
 * <b>Interface Name</b>: ApplicationConfigurationRepository<br>
 * <b>Description</b>: JPA repository for Application Configuration.<br>
 * @author nishad.chayanakhawa
 */
public interface ApplicationConfigurationRepository extends JpaRepository<ApplicationConfiguration,Long>{
	//standard data access methods are handled by jpa automatically. No need to declare and define.
}
