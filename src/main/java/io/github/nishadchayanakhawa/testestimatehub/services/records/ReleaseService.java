package io.github.nishadchayanakhawa.testestimatehub.services.records;

import java.util.List;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import io.github.nishadchayanakhawa.testestimatehub.repositories.records.ReleaseRepository;
import io.github.nishadchayanakhawa.testestimatehub.services.records.exceptions.DuplicateReleaseException;
import io.github.nishadchayanakhawa.testestimatehub.model.dto.records.ReleaseDTO;
import io.github.nishadchayanakhawa.testestimatehub.model.records.Release;

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

	public ReleaseDTO save(ReleaseDTO releaseToSaveDTO) {
		logger.debug("Release to save: {}", releaseToSaveDTO);
		try {
			ReleaseDTO savedReleaseDTO = modelMapper.map(
					this.releaseRepository.save(modelMapper.map(releaseToSaveDTO, Release.class)), ReleaseDTO.class);
			logger.debug("Saved release : {}", savedReleaseDTO);
			return savedReleaseDTO;
		} catch (DataIntegrityViolationException e) {
			// throw exception when change type name is not unique
			throw (DuplicateReleaseException) new DuplicateReleaseException(
					String.format("Release '%s' already exists.", releaseToSaveDTO.getIdentifier())).initCause(e);
		}
	}
	
	public List<ReleaseDTO> getAll() {
		//get list of saved release records
		logger.debug("Retreiving all release records");
		List<ReleaseDTO> releases = this.releaseRepository.findAll().stream()
				.map(release -> modelMapper.map(release, ReleaseDTO.class)).toList();
		logger.debug("Releases: {}", releases);
		//return the list
		return releases;
	}
	
	public ReleaseDTO get(Long id) {
		//retreive release based on id
		logger.debug("Retreiving release for id {}",id);
		ReleaseDTO releaseDTO=modelMapper.map(this.releaseRepository.findById(id).orElseThrow(), ReleaseDTO.class);
		logger.debug("Retreived release: {}",releaseDTO);
		//return release
		return releaseDTO;
	}
	
	public void delete(ReleaseDTO releaseToDeleteDTO) {
		//delete release record
		logger.debug("Deleting release: {}",releaseToDeleteDTO);
		this.releaseRepository.deleteById(releaseToDeleteDTO.getId());
		logger.debug("Deleted release successfully.");
	}
}
