package io.github.nishadchayanakhawa.testestimatehub.repositories.records;

import org.springframework.data.jpa.repository.JpaRepository;

import io.github.nishadchayanakhawa.testestimatehub.model.records.Requirement;

public interface RequirementRepository extends JpaRepository<Requirement,Long>{
	
}
