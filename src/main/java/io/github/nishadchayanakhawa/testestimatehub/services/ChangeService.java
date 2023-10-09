package io.github.nishadchayanakhawa.testestimatehub.services;

//import section
//java-util
import java.util.List;
import java.util.Set;
import java.util.HashSet;
import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;
import java.math.BigDecimal;
import java.math.RoundingMode;

//model mapper
import org.modelmapper.ModelMapper;
//logger
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
//spring
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import io.github.nishadchayanakhawa.testestimatehub.repositories.ChangeRepository;
import io.github.nishadchayanakhawa.testestimatehub.repositories.RequirementRepository;
import io.github.nishadchayanakhawa.testestimatehub.repositories.TestTypeRepository;
import io.github.nishadchayanakhawa.testestimatehub.services.records.exceptions.DuplicateChangeException;
import io.github.nishadchayanakhawa.testestimatehub.services.records.exceptions.DuplicateChangeImpactException;
import io.github.nishadchayanakhawa.testestimatehub.services.records.exceptions.DuplicateRequirementException;
import io.github.nishadchayanakhawa.testestimatehub.model.dto.ChangeDTO;
import io.github.nishadchayanakhawa.testestimatehub.model.dto.GeneralConfigurationDTO;
import io.github.nishadchayanakhawa.testestimatehub.model.dto.RequirementDTO;
import io.github.nishadchayanakhawa.testestimatehub.model.Change;
import io.github.nishadchayanakhawa.testestimatehub.model.Complexity;
import io.github.nishadchayanakhawa.testestimatehub.model.Estimation;
import io.github.nishadchayanakhawa.testestimatehub.model.EstimationDetail;
import io.github.nishadchayanakhawa.testestimatehub.model.Requirement;
import io.github.nishadchayanakhawa.testestimatehub.model.TestType;
import io.github.nishadchayanakhawa.testestimatehub.model.UseCase;

/**
 * <b>Class Name</b>: ChangeService<br>
 * <b>Description</b>: Service for change entity.<br>
 * 
 * @author nishad.chayanakhawa
 */
@Service
public class ChangeService {
	// logger
	private static final Logger logger = LoggerFactory.getLogger(ChangeService.class);

	// change type repository
	@Autowired
	private ChangeRepository changeRepository;

	@Autowired
	private TestTypeRepository testTypeRepository;

	// requirement repository
	@Autowired
	private RequirementRepository requirementRepository;

	@Autowired
	private GeneralConfigurationService generalConfigurationService;

	// model mapper
	@Autowired
	private ModelMapper modelMapper;

	/**
	 * <b>Method Name</b>: save<br>
	 * <b>Description</b>: Save change record.<br>
	 * 
	 * @param changeToSaveDTO change record to save as
	 *                        {@link io.github.nishadchayanakhawa.testestimatehub.model.dto.ChangeDTO
	 *                        ChangeDTO}
	 * @return saved change instance as
	 *         {@link io.github.nishadchayanakhawa.testestimatehub.model.dto.ChangeDTO
	 *         ChangeDTO}
	 */
	public ChangeDTO save(ChangeDTO changeToSaveDTO) {
		logger.debug("Change to save: {}", changeToSaveDTO);
		try {
			// save change record
			ChangeDTO savedChangeDTO = modelMapper
					.map(this.changeRepository.save(modelMapper.map(changeToSaveDTO, Change.class)), ChangeDTO.class);
			logger.debug("Saved change : {}", savedChangeDTO);
			// return saved change record
			return savedChangeDTO;
		} catch (DataIntegrityViolationException e) {
			if (e.getMessage().contains("TEH_UNIQUE_REQUIREMENT_PER_CHANGE")) {
				// throw exception when requirement identifier is not unique
				throw (DuplicateRequirementException) new DuplicateRequirementException(
						"Please review identifiers used for requirements. Single change record cannot have duplicate identifiers.")
						.initCause(e);
			} else if (e.getMessage().contains("PUBLIC.PRIMARY_KEY_6E ON PUBLIC.TEH_CHANGE_IMPACT")) {
				// throw exception when app configuration identifier is not unique
				throw (DuplicateChangeImpactException) new DuplicateChangeImpactException(
						"Please remove duplicate entries from impacted area.").initCause(e);
			} else {
				// throw exception when change identifier is not unique
				logger.error("[][]: {}", e.getMessage());
				throw (DuplicateChangeException) new DuplicateChangeException(
						String.format("Change '%s' already exists.", changeToSaveDTO.getIdentifier())).initCause(e);
			}
		}
	}

	/**
	 * <b>Method Name</b>: getAll<br>
	 * <b>Description</b>: Get list of all saved changes.<br>
	 * 
	 * @return {@link java.util.List List} of
	 *         {@link io.github.nishadchayanakhawa.testestimatehub.model.dto.ChangeDTO
	 *         ChangeDTO}
	 */
	public List<ChangeDTO> getAll() {
		// get list of saved change records
		logger.debug("Retreiving all change records");
		List<ChangeDTO> changes = this.changeRepository.findAll().stream()
				.map(change -> modelMapper.map(change, ChangeDTO.class)).toList();
		logger.debug("Changes: {}", changes);
		// return the list
		return changes;
	}

	/**
	 * <b>Method Name</b>: get<br>
	 * <b>Description</b>: Retreive change record based on id.<br>
	 * 
	 * @param id for change record as {@link java.lang.Long Long}
	 * @return matching change record as
	 *         {@link io.github.nishadchayanakhawa.testestimatehub.model.dto.ChangeDTO
	 *         ChangeDTO}
	 */
	public ChangeDTO get(Long id) {
		// retreive change based on id
		logger.debug("Retreiving change for id {}", id);
		ChangeDTO changeDTO = modelMapper.map(this.changeRepository.findById(id).orElseThrow(), ChangeDTO.class);
		logger.debug("Retreived change: {}", changeDTO);
		// return change
		return changeDTO;
	}

	/**
	 * <b>Method Name</b>: delete<br>
	 * <b>Description</b>: Delete change record<br>
	 * 
	 * @param changeToDeleteDTO change record to delete as
	 *                          {@link io.github.nishadchayanakhawa.testestimatehub.model.dto.ChangeDTO
	 *                          ChangeDTO}. Only id is required, other fields are
	 *                          ignored and hence can be set to null.
	 */
	public void delete(ChangeDTO changeToDeleteDTO) {
		// delete change record
		logger.debug("Deleting change: {}", changeToDeleteDTO);
		this.changeRepository.deleteById(changeToDeleteDTO.getId());
		logger.debug("Deleted change successfully.");
	}

	/**
	 * <b>Method Name</b>: getRequirement<br>
	 * <b>Description</b>: Get requirement by id<br>
	 * @param id id as {@link java.lang.Long Long}
	 * @return requirement as 
	 * {@link io.github.nishadchayanakhawa.testestimatehub.model.dto.RequirementDTO RequirementDTO}
	 */
	public RequirementDTO getRequirement(Long id) {
		logger.debug("Looking up requirement with id: {}", id);
		RequirementDTO requirement = modelMapper.map(this.requirementRepository.findById(id).orElseThrow(),
				RequirementDTO.class);
		logger.debug("Requirement located: {}", requirement);
		return requirement;
	}

	/**
	 * <b>Method Name</b>: saveUseCases<br>
	 * <b>Description</b>: Save use cases.<br>
	 * @param requirementWithUseCasesToSave as 
	 * {@link io.github.nishadchayanakhawa.testestimatehub.model.dto.RequirementDTO RequirementDTO}
	 * @return requirement with saved use cases as 
	 * {@link io.github.nishadchayanakhawa.testestimatehub.model.dto.RequirementDTO RequirementDTO}
	 */
	public RequirementDTO saveUseCases(RequirementDTO requirementWithUseCasesToSave) {
		logger.debug("Saving use cases within requirement: {}", requirementWithUseCasesToSave);
		//get original requirement
		RequirementDTO originalRequirement = this.getRequirement(requirementWithUseCasesToSave.getId());
		//set use cases
		originalRequirement.setUseCases(requirementWithUseCasesToSave.getUseCases());

		//set requirement id if it doesnt exist
		originalRequirement.getUseCases().stream().forEach(useCase -> {
			if (useCase.getRequirementId() == null) {
				useCase.setRequirementId(originalRequirement.getId());
			}
		});

		//save requirement with attached use cases
		RequirementDTO savedRequirementWithUseCases = modelMapper.map(
				this.requirementRepository.save(modelMapper.map(originalRequirement, Requirement.class)),
				RequirementDTO.class);
		logger.debug("Saved use cases within requirement: {}", savedRequirementWithUseCases);
		return savedRequirementWithUseCases;
	}

	/**
	 * <b>Method Name</b>: calculateEffectiveComplexity<br>
	 * <b>Description</b>: Calculate effective complexity.<br>
	 * @param useCase as 
	 * {@link io.github.nishadchayanakhawa.testestimatehub.model.UseCase UseCase}
	 * @param generalConfiguration as 
	 * {@link io.github.nishadchayanakhawa.testestimatehub.model.dto.GeneralConfigurationDTO GeneralConfigurationDTO}
	 * @return complexity as 
	 * {@link io.github.nishadchayanakhawa.testestimatehub.model.Complexity Complexity}
	 */
	private Complexity calculateEffectiveComplexity(UseCase useCase, GeneralConfigurationDTO generalConfiguration) {
		//calculate effective complexity as-
		//corresponding ordinal position +1(as ordinal starts with 0), multiply with weightage from general configuration
		//round off to nearest integer
		//reduce 1 to normalize ordinal +1 increment applied earlier
		int effectiveComplexityOrdinal = ((int) Math.round(((useCase.getTestConfigurationComplexity().ordinal() + 1)
				* (generalConfiguration.getTestConfigurationComplexityPercentage() / 100))
				+ ((useCase.getTestDataSetupComplexity().ordinal() + 1)
						* (generalConfiguration.getTestDataComplexityPercentage() / 100))
				+ ((useCase.getTestTransactionComplexity().ordinal() + 1)
						* (generalConfiguration.getTestTransactionComplexityPercentage() / 100))
				+ ((useCase.getTestValidationComplexity().ordinal() + 1)
						* (generalConfiguration.getTestValidationComplexityPercentage() / 100)))
				- 1);
		//return value corresponding to calculated effective ordinal value
		return Complexity.values()[effectiveComplexityOrdinal];
	}

	/**
	 * <b>Method Name</b>: round<br>
	 * <b>Description</b>: Rounds double to 2 precision place value.<br>
	 * @param value value to round
	 * @return rounded value
	 */
	private static double round(double value) {
		BigDecimal bd = new BigDecimal(Double.toString(value));
		bd = bd.setScale(2, RoundingMode.HALF_UP);
		return bd.doubleValue();
	}

	/**
	 * <b>Method Name</b>: calculateEstimate<br>
	 * <b>Description</b>: Calculate individual estimation with detail.<br>
	 * @param useCase use case as 
	 * {@link io.github.nishadchayanakhawa.testestimatehub.model.UseCase UseCase}
	 * @param testType associated test type as 
	 * {@link io.github.nishadchayanakhawa.testestimatehub.model.TestType TestType}
	 * @param testCaseCountModifierByChangeType test case count modifier based on change type
	 * @return calculated estimation detail as 
	 * {@link io.github.nishadchayanakhawa.testestimatehub.model.EstimationDetail EstimationDetail}
	 */
	private EstimationDetail calculateEstimate(UseCase useCase, TestType testType,
			double testCaseCountModifierByChangeType) {
		EstimationDetail estimate = new EstimationDetail();
		//calculate test case count as-
		//base functiionality test count * by data variation * test type relative %
		estimate.setTestCaseCount((int) Math
				.round(((useCase.getBusinessFunctionality().getBaseTestScriptCount() * useCase.getDataVariationCount())
						* (testType.getRelativeTestCaseCountPercentage() / 100)) * testCaseCountModifierByChangeType));
		//execution count same as test case count
		estimate.setExecutionCount(estimate.getTestCaseCount());
		//re-execution count as - execution count * re-execution % for corresponding test type
		estimate.setReExecutionCount(
				(int) Math.round(estimate.getExecutionCount() * (testType.getReExecutionPercentage() / 100)));
		//additional cycle execution count as - execution count * additional cycle execution % for corresponding test type
		estimate.setAdditionalCycleExecutionCount((int) Math
				.round(estimate.getExecutionCount() * (testType.getAdditionalCycleExecutionPercentage() / 100)));
		//total execution count as - exec + re-exe + addnl cycle execution
		estimate.setTotalExecutionCount(estimate.getExecutionCount() + estimate.getReExecutionCount()
				+ estimate.getAdditionalCycleExecutionCount());

		//set use case od to estimate for bi-directional relation
		estimate.setUseCaseId(useCase.getId());
		//set test type
		estimate.setTestType(testType);

		//get general  configuration record
		GeneralConfigurationDTO generalConfiguration = this.generalConfigurationService.get();
		//get effective complexity
		Complexity effectiveComplexity = this.calculateEffectiveComplexity(useCase, generalConfiguration);

		//set design efforts as test case count / productivity for corresponding effective complexity
		estimate.setDesignEfforts(ChangeService.round((estimate.getTestCaseCount()
				/ generalConfiguration.getTestDesignProductivity().get(effectiveComplexity)) * 8));
		//set execution efforts as test case count / productivity for corresponding effective complexity
		estimate.setExecutionEfforts(ChangeService.round((estimate.getTotalExecutionCount()
				/ generalConfiguration.getTestExecutionProductivity().get(effectiveComplexity)) * 8));
		//total efforts as design + execution efforts
		estimate.setTotalEfforts(estimate.getDesignEfforts() + estimate.getExecutionEfforts());

		return estimate;
	}

	/**
	 * <b>Method Name</b>: calculateEstimates<br>
	 * <b>Description</b>: Calculate estimation details for given use case.<br>
	 * @param useCase use case as 
	 * {@link io.github.nishadchayanakhawa.testestimatehub.model.UseCase UseCase}
	 * @param testCaseCountModifierByChangeType test case count modifier based on change type
	 * @return {@link java.util.Set Set} of 
	 * {@link io.github.nishadchayanakhawa.testestimatehub.model.EstimationDetail EstimationDetail}
	 */
	private Set<EstimationDetail> calculateEstimates(UseCase useCase, double testCaseCountModifierByChangeType) {
		Set<EstimationDetail> estimations = new HashSet<>();
		//for each test type, calculate and add estimate
		useCase.getApplicableTestTypes().stream().forEach(testType -> estimations
				.add(this.calculateEstimate(useCase, testType, testCaseCountModifierByChangeType)));
		return estimations;
	}

	/**
	 * <b>Method Name</b>: generateEstimates<br>
	 * <b>Description</b>: Generate estimates for a given change.<br>
	 * @param changeId id of change as {@link java.lang.Long Long}
	 * @return change record with estimates attached as 
	 * {@link io.github.nishadchayanakhawa.testestimatehub.model.dto.ChangeDTO ChangeDTO}
	 */
	public ChangeDTO generateEstimates(Long changeId) {
		Change change = this.changeRepository.findById(changeId).orElseThrow();
		//map estimation details by test type
		Map<Long, List<EstimationDetail>> estimationsByTestTypeMap = new HashMap<>();

		Set<Estimation> estimations = new HashSet<>();

		//populate estimation detail map against corresponding test type
		change.getRequirements().stream().forEach(requirement -> requirement.getUseCases().stream().forEach(useCase -> {

			useCase.setEstimations(this.calculateEstimates(useCase, change.getChangeType().getTestCaseCountModifier()));
			useCase.getEstimations().stream().forEach(estimationDetail -> {
				if (!estimationsByTestTypeMap.containsKey(estimationDetail.getTestType().getId())) {
					estimationsByTestTypeMap.put(estimationDetail.getTestType().getId(), new ArrayList<>());
				}
				estimationsByTestTypeMap.get(estimationDetail.getTestType().getId()).add(estimationDetail);
			});
		}));

		//for each test type, calculate cululative values
		this.testTypeRepository.findAll().stream().forEach(testType -> {
			if (estimationsByTestTypeMap.containsKey(testType.getId())) {
				Estimation estimation = new Estimation();
				//set change id and test type for estimation
				estimation.setChangeId(change.getId());
				estimation.setTestType(testType);
				//sum individual counts under same test type and set in estimation record
				//set test case count
				estimation.setTestCaseCount(estimationsByTestTypeMap.get(testType.getId()).stream()
						.mapToInt(EstimationDetail::getTestCaseCount).sum());
				//execution count
				estimation.setExecutionCount(estimationsByTestTypeMap.get(testType.getId()).stream()
						.mapToInt(EstimationDetail::getExecutionCount).sum());
				//re-execution count
				estimation.setReExecutionCount(estimationsByTestTypeMap.get(testType.getId()).stream()
						.mapToInt(EstimationDetail::getReExecutionCount).sum());
				//additional cycle execution count
				estimation.setAdditionalCycleExecutionCount(estimationsByTestTypeMap.get(testType.getId()).stream()
						.mapToInt(EstimationDetail::getAdditionalCycleExecutionCount).sum());
				//total execution count
				estimation.setTotalExecutionCount(estimationsByTestTypeMap.get(testType.getId()).stream()
						.mapToInt(EstimationDetail::getTotalExecutionCount).sum());
				//design efforts
				estimation.setDesignEfforts(estimationsByTestTypeMap.get(testType.getId()).stream()
						.mapToDouble(EstimationDetail::getDesignEfforts).sum());
				//execution efforts
				estimation.setExecutionEfforts(estimationsByTestTypeMap.get(testType.getId()).stream()
						.mapToDouble(EstimationDetail::getExecutionEfforts).sum());
				//total efforts
				estimation.setTotalEfforts(estimation.getDesignEfforts() + estimation.getExecutionEfforts());
				//add estimation to set
				estimations.add(estimation);
			}
		});

		//attach set of estimations to change record
		change.setEstimations(estimations);

		//calculate total values from estimation records and set to change
		//total test cases
		change.setTotalTestCases(change.getEstimations().stream().mapToInt(Estimation::getTestCaseCount).sum());
		//total executions
		change.setTotalExecutions(change.getEstimations().stream().mapToInt(Estimation::getTotalExecutionCount).sum());
		//design + execution efforts sum
		change.setEfforts(change.getEstimations().stream().mapToDouble(Estimation::getTotalEfforts).sum());

		//calculate planning effort as total effort * planning effort for corresponding change type
		change.setTestPlanningEfforts(ChangeService.round(
				change.getEfforts() * (change.getChangeType().getTestPlanningEffortAllocationPercentage() / 100)));
		//calculate preparation effort as total effort * preparation effort for corresponding change type
		change.setTestPreparationEfforts(ChangeService.round(
				change.getEfforts() * (change.getChangeType().getTestPreparationEffortAllocationPercentage() / 100)));
		//calculate management effort as total effort * management effort for corresponding change type
		change.setManagementEfforts(ChangeService
				.round(change.getEfforts() * (change.getChangeType().getManagementEffortAllocationPercentage() / 100)));

		//final efforts by summing design, exec, planning, preparation and management efforts
		change.setFinalEfforts(change.getEfforts() + change.getTestPlanningEfforts()
				+ change.getTestPreparationEfforts() + change.getManagementEfforts());

		//save the change record and return the same.
		return this.save(modelMapper.map(change, ChangeDTO.class));
	}
}