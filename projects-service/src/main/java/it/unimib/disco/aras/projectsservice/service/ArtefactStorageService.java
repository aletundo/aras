package it.unimib.disco.aras.projectsservice.service;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.data.rest.core.event.AfterSaveEvent;
import org.springframework.data.rest.core.event.BeforeSaveEvent;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import it.unimib.disco.aras.projectsservice.entity.Project;
import it.unimib.disco.aras.projectsservice.repository.ProjectRepository;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ArtefactStorageService {
	@Autowired
	private ProjectRepository projectRepository;

	private static final String UPLOADS_DIR = "/data/artefacts/";

	@Autowired
	private ApplicationEventPublisher publisher;

	public void store(String projectId, String versionId, MultipartFile artefact) throws IOException {
		File versionArtefactsPath = new File(UPLOADS_DIR + projectId + "/" + versionId + "/");

		if (!versionArtefactsPath.exists()) {
			boolean created = versionArtefactsPath.mkdirs();
			if (created) {
				log.debug("versionArtefactsAbsolutePath = {}", versionArtefactsPath.getAbsolutePath());
			} else {
				throw new RuntimeException("Cannot create " + versionArtefactsPath.getAbsolutePath());
			}
		}

		String filepath = versionArtefactsPath.getPath() + "/" + artefact.getOriginalFilename();

		byte[] bytes = artefact.getBytes();
		Path path = Paths.get(filepath);
		Files.write(path, bytes);

		Project project = projectRepository.findById(projectId).orElseThrow(() -> new ResourceNotFoundException());
		project.getVersions().forEach(v -> {
			if (v.getId().equals(versionId)) {
				v.setArtefactsPath(filepath);
			}
		});
		
		publisher.publishEvent(new BeforeSaveEvent(project));
		projectRepository.save(project);
		publisher.publishEvent(new AfterSaveEvent(project));
	}

	public Resource load(String projectId, String versionId) throws MalformedURLException {
		Project project = projectRepository.findByVersionId(versionId);

		Path filepath = Paths.get(project.getVersion(versionId).getArtefactsPath()).toAbsolutePath().normalize();
		Resource resource = new UrlResource(filepath.toUri());
		
		if (resource.exists()) {
			return resource;
		} else {
			throw new ResourceNotFoundException();
		}
	}
}
