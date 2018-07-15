package it.unimib.disco.aras.projectsservice.repository;

import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.core.annotation.HandleBeforeCreate;
import org.springframework.data.rest.core.annotation.HandleBeforeSave;
import org.springframework.data.rest.core.annotation.RepositoryEventHandler;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.stereotype.Service;

import it.unimib.disco.aras.projectsservice.entity.Project;
import it.unimib.disco.aras.projectsservice.entity.Version;

/**
 * The Class ProjectEventHandler.
 */
@Service
@RepositoryEventHandler(Project.class)
public class ProjectEventHandler {

	/** The project repository. */
	@Autowired
	private ProjectRepository projectRepository;

	/**
	 * Handle creating project.
	 *
	 * @param project
	 *            the project
	 */
	@HandleBeforeCreate
	public void handleCreatingProject(Project project) {
		if (checkInputString(project.getName()) || checkInputString(project.getDescription())) {
			throw new HttpMessageNotReadableException("Validation failed");
		}
		
		final Date now = Date.from(Instant.now());
		project.setCreatedAt(now);
		project.setUpdatedAt(now);
	}

	/**
	 * Handle saving project.
	 *
	 * @param project
	 *            the project
	 */
	@HandleBeforeSave
	public void handleSavingProject(Project project) {
		final Date now = Date.from(Instant.now());
		Project currentProject = projectRepository.findById(project.getId()).get();
		
		List<Version> versions = project.getVersions().stream().map(v -> {
			if(null == v.getId()) {
				if(!isValidVersion(v)) {
					throw new HttpMessageNotReadableException("Validation failed");
				}
				v.setId(new ObjectId().toString());
				v.setCreatedAt(now);
				v.setUpdatedAt(now);
			} else if(isDirty(currentProject.getVersions(), v)) {
				v.setUpdatedAt(now);
			}
			return v;
		}).collect(Collectors.toList());
		
		project.setVersions(versions);
		project.setUpdatedAt(now);
	}
	
	/**
	 * Checks if is dirty.
	 *
	 * @param currentVersions
	 *            the current versions
	 * @param v
	 *            the v
	 * @return true, if is dirty
	 */
	private boolean isDirty(List<Version> currentVersions, Version v) {
		boolean isDirty = false;
		for(Version cv : currentVersions) {
			if(cv.getId().equals(v.getId()) && !cv.equals(v)) {
				isDirty = true;
			}
		}
		return isDirty || !currentVersions.contains(v);
	}
	
	/**
	 * Check input string.
	 *
	 * @param input
	 *            the input
	 * @return true, if successful
	 */
	private boolean checkInputString(String input) {
		return (input == null || input.trim().length() == 0);
	}
	
	/**
	 * Checks if is valid version.
	 *
	 * @param version
	 *            the version
	 * @return true, if is valid version
	 */
	private boolean isValidVersion(Version version) {
		return !checkInputString(version.getName()) && !checkInputString(version.getDescription());
	}
}
