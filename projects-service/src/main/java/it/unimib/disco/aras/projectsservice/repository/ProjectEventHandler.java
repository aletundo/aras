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
import org.springframework.stereotype.Service;

import it.unimib.disco.aras.projectsservice.entity.Project;
import it.unimib.disco.aras.projectsservice.entity.Version;
import lombok.extern.slf4j.Slf4j;

@Service
@RepositoryEventHandler(Project.class)
@Slf4j
public class ProjectEventHandler {

	@Autowired
	private ProjectRepository projectRepository;

	@HandleBeforeCreate
	public void handleCreatingProject(Project project) {
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
				v.setId(new ObjectId().toString());
				v.setCreatedAt(now);
				v.setUpdatedAt(now);
			} else if(!currentProject.getVersions().contains(v)) {
				v.setUpdatedAt(now);
			}
			return v;
		}).collect(Collectors.toList());
		
		project.setVersions(versions);
		project.setUpdatedAt(now);
		
		
//		Project currentProject = projectRepository.findById(project.getId()).get();
//
//		List<Version> updatedOrAdded = new ArrayList<>();
//
//		if (null == currentProject.getVersions()) {
//			updatedOrAdded = project.getVersions();
//		} else {
//			updatedOrAdded = new ArrayList<>(
//					CollectionUtils.disjunction(project.getVersions(), currentProject.getVersions()));
//		}
//
//		if (!updatedOrAdded.isEmpty()) {
//			
//			updatedOrAdded = updatedOrAdded.parallelStream().map(v -> {
//				if (null == v.getId()) {
//					v.setId(new ObjectId().toString());
//					v.setCreatedAt(now);
//					v.setUpdatedAt(now);
//				} else if (!currentProject.getVersions().contains(v)) {
//					v.setUpdatedAt(now);
//				}
//				return v;
//			}).collect(Collectors.toList());
//			
//			updatedOrAdded.addAll(CollectionUtils.subtract(project.getVersions(), updatedOrAdded));
//		}
//		
//		project.setVersions(updatedOrAdded);
	}
}
