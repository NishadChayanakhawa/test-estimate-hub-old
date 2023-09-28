package io.github.nishadchayanakhawa.testestimatehub.repositories.records;

//import section
//jpa
import org.springframework.data.jpa.repository.JpaRepository;
//entity
import io.github.nishadchayanakhawa.testestimatehub.model.records.Change;

/**
 * <b>Class Name</b>: ChangeRepository<br>
 * <b>Description</b>: JPA repository for Change records<br>
 * 
 * @author nishad.chayanakhawa
 */
public interface ChangeRepository extends JpaRepository<Change, Long> {
	// Functions will be auto generated by JPA
}
