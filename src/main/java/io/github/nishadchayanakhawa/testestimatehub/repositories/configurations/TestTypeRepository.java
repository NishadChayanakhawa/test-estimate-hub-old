package io.github.nishadchayanakhawa.testestimatehub.repositories.configurations;

//import section
//spring data library
import org.springframework.data.jpa.repository.JpaRepository;
//entity
import io.github.nishadchayanakhawa.testestimatehub.model.configurations.TestType;

public interface TestTypeRepository extends JpaRepository<TestType,Long>{

}
