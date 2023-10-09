package io.github.nishadchayanakhawa.testestimatehub.services;

//import section
//java-util
import java.util.List;
//model mapper
import org.modelmapper.ModelMapper;
//logger
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
//spring
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import io.github.nishadchayanakhawa.testestimatehub.services.records.exceptions.DuplicateReleaseException;
import io.github.nishadchayanakhawa.testestimatehub.model.Release;
import io.github.nishadchayanakhawa.testestimatehub.model.dto.ReleaseDTO;
import io.github.nishadchayanakhawa.testestimatehub.repositories.ReleaseRepository;

/**
 * <b>Class Name</b>: ReleaseService<br>
 * <b>Description</b>: Service for release entity.<br>
 * @author nishad.chayanakhawa
 */
@Service
public class ReleaseService {
	// logger
	private static final Logger logger = LoggerFactory.getLogger(ReleaseService.class);

	// change type repository
	@Autowired
	private ReleaseRepository releaseRepository;

	// model mapper
	@Autowired
	private ModelMapper modelMapper;

	/**
	 * <b>Method Name</b>: save<br>
	 * <b>Description</b>: Save release record.<br>
	 * @param releaseToSaveDTO release record to save as 
	 * {@link io.github.nishadchayanakhawa.testestimatehub.model.dto.ReleaseDTO ReleaseDTO}
	 * @return saved release instance as 
	 * {@link io.github.nishadchayanakhawa.testestimatehub.model.dto.ReleaseDTO ReleaseDTO}
	 */
	public ReleaseDTO save(ReleaseDTO releaseToSaveDTO) {
		logger.debug("Release to save: {}", releaseToSaveDTO);
		try {
			//save release record
			ReleaseDTO savedReleaseDTO = modelMapper.map(
					this.releaseRepository.save(modelMapper.map(releaseToSaveDTO, Release.class)), ReleaseDTO.class);
			logger.debug("Saved release : {}", savedReleaseDTO);
			//return saved release record
			return savedReleaseDTO;
		} catch (DataIntegrityViolationException e) {
			// throw exception when release identifier is not unique
			throw (DuplicateReleaseException) new DuplicateReleaseException(
					String.format("Release '%s' already exists.", releaseToSaveDTO.getIdentifier())).initCause(e);
		}
	}

	/**
	 * <b>Method Name</b>: getAll<br>
	 * <b>Description</b>: Get list of all saved releases.<br>
	 * @return {@link java.util.List List} of 
	 * {@link io.github.nishadchayanakhawa.testestimatehub.model.dto.ReleaseDTO ReleaseDTO}
	 */
	public List<ReleaseDTO> getAll() {
		// get list of saved release records
		logger.debug("Retreiving all release records");
		List<ReleaseDTO> releases = this.releaseRepository.findAll().stream()
				.map(release -> modelMapper.map(release, ReleaseDTO.class)).toList();
		logger.debug("Releases: {}", releases);
		// return the list
		return releases;
	}

	/**
	 * <b>Method Name</b>: get<br>
	 * <b>Description</b>: Retreive release record based on id.<br>
	 * @param id for release record as {@link java.lang.Long Long}
	 * @return matching release record as 
	 * {@link io.github.nishadchayanakhawa.testestimatehub.model.dto.ReleaseDTO ReleaseDTO}
	 */
	public ReleaseDTO get(Long id) {
		// retreive release based on id
		logger.debug("Retreiving release for id {}", id);
		ReleaseDTO releaseDTO = modelMapper.map(this.releaseRepository.findById(id).orElseThrow(), ReleaseDTO.class);
		logger.debug("Retreived release: {}", releaseDTO);
		// return release
		return releaseDTO;
	}

	/**
	 * <b>Method Name</b>: delete<br>
	 * <b>Description</b>: Delete release record<br>
	 * @param releaseToDeleteDTO release record to delete as 
	 * {@link io.github.nishadchayanakhawa.testestimatehub.model.dto.ReleaseDTO ReleaseDTO}. 
	 * Only id is required, other fields are ignored and hence can be set to null.
	 */
	public void delete(ReleaseDTO releaseToDeleteDTO) {
		// delete release record
		logger.debug("Deleting release: {}", releaseToDeleteDTO);
		this.releaseRepository.deleteById(releaseToDeleteDTO.getId());
		logger.debug("Deleted release successfully.");
	}
}
