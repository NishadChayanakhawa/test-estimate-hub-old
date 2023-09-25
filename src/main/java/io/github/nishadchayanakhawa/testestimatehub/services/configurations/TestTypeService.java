package io.github.nishadchayanakhawa.testestimatehub.services.configurations;

import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionSystemException;

import io.github.nishadchayanakhawa.testestimatehub.model.configurations.TestType;
import io.github.nishadchayanakhawa.testestimatehub.model.dto.configurations.TestTypeDTO;
import io.github.nishadchayanakhawa.testestimatehub.repositories.configurations.TestTypeRepository;
import io.github.nishadchayanakhawa.testestimatehub.services.configurations.exceptions.DuplicateTestTypeException;
import io.github.nishadchayanakhawa.testestimatehub.services.configurations.exceptions.TestTypeTransactionException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;

@Service
public class TestTypeService {
	private static final Logger logger = LoggerFactory.getLogger(TestTypeService.class);

	@Autowired
	private TestTypeRepository testTypeRepository;

	@Autowired
	private ModelMapper modelMapper;

	public TestTypeDTO save(TestTypeDTO testTypeToSaveDTO) {
		logger.debug("Saving Test Type: {}", testTypeToSaveDTO);
		try {
			TestTypeDTO testTypeSavedDTO = modelMapper.map(
					this.testTypeRepository.save(modelMapper.map(testTypeToSaveDTO, TestType.class)),
					TestTypeDTO.class);
			logger.debug("Saved Test Type: {}", testTypeSavedDTO);
			return testTypeSavedDTO;
		} catch (DataIntegrityViolationException e) {
			// throw exception when test type name is not unique
			throw (DuplicateTestTypeException) new DuplicateTestTypeException(
					String.format("Test type '%s' already exists.", testTypeToSaveDTO.getName())).initCause(e);
		} catch (TransactionSystemException e) {
			// retreive constraint violation messages
			ConstraintViolationException re = (ConstraintViolationException) e.getRootCause();
			Set<ConstraintViolation<?>> violations = re.getConstraintViolations();
			String messages = violations.stream().map(ConstraintViolation::getMessage).collect(Collectors.joining(";"));
			// throw exception in case of constraint violation
			throw (TestTypeTransactionException) new TestTypeTransactionException(messages).initCause(e);
		}
	}

	public List<TestTypeDTO> getAll() {
		logger.debug("Retreiving all test type records");
		List<TestTypeDTO> testTypes = this.testTypeRepository.findAll().stream()
				.map(testType -> modelMapper.map(testType, TestTypeDTO.class)).toList();
		logger.debug("Test types: {}", testTypes);
		return testTypes;
	}
	
	public TestTypeDTO get(Long id) {
		logger.debug("Retreiving test type for id {}",id);
		TestTypeDTO testTypeDTO=modelMapper.map(this.testTypeRepository.findById(id).orElseThrow(), TestTypeDTO.class);
		logger.debug("Retreived test type: {}",testTypeDTO);
		return testTypeDTO;
	}
	
	public void delete(TestTypeDTO testTypeToDeleteDTO) {
		logger.debug("Deleting test type: {}",testTypeToDeleteDTO);
		this.testTypeRepository.deleteById(testTypeToDeleteDTO.getId());
		logger.debug("Deleted test type successfully.");
	}
}
