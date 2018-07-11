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

@Service
@RepositoryEventHandler(Project.class)
public class ProjectEventHandler {

	@Autowired
	private ProjectRepository projectRepository;

	@HandleBeforeCreate
	public void handleCreatingProject(Project project) {
		if (checkInputString(project.getName()) || checkInputString(project.getDescription())) {
			throw new HttpMessageNotReadableException("Validation failed");
		}
		
		final Date now = Date.from(Instant.now());
		project.setCreatedAt(now);
		project.setUpdatedAt(now);
	}

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
	
	private boolean isDirty(List<Version> currentVersions, Version v) {
		boolean isDirty = false;
		for(Version cv : currentVersions) {
			if(cv.getId().equals(v.getId()) && !cv.equals(v)) {
				isDirty = true;
			}
		}
		return isDirty || !currentVersions.contains(v);
	}
	
	private boolean checkInputString(String input) {
		return (input == null || input.trim().length() == 0);
	}
	
	private boolean isValidVersion(Version version) {
		return !checkInputString(version.getName()) && !checkInputString(version.getDescription());
	}
}
