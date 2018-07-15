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

/**
 * The Class ArtefactStorageService.
 */
@Service

/** The Constant log. */
@Slf4j
public class ArtefactStorageService {
	
	/** The project repository. */
	@Autowired
	private ProjectRepository projectRepository;

	/** The Constant UPLOADS_DIR. */
	private static final String UPLOADS_DIR = "/data/artefacts/";

	/** The publisher. */
	@Autowired
	private ApplicationEventPublisher publisher;

	/**
	 * Store.
	 *
	 * @param projectId
	 *            the project id
	 * @param versionId
	 *            the version id
	 * @param artefact
	 *            the artefact
	 * @return the project
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public Project store(String projectId, String versionId, MultipartFile artefact) throws IOException {
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

		Project project = projectRepository.findById(projectId).orElseThrow(ResourceNotFoundException::new);
		project.getVersions().forEach(v -> {
			if (v.getId().equals(versionId)) {
				v.setArtefactsPath(filepath);
			}
		});

		publisher.publishEvent(new BeforeSaveEvent(project));
		Project savedProject = projectRepository.save(project);
		publisher.publishEvent(new AfterSaveEvent(project));
		return savedProject;
	}

	/**
	 * Load.
	 *
	 * @param versionId
	 *            the version id
	 * @return the resource
	 * @throws MalformedURLException
	 *             the malformed URL exception
	 */
	public Resource load(String versionId) throws MalformedURLException {
		Project project = projectRepository.findByVersionId(versionId)
				.orElseThrow(ResourceNotFoundException::new);

		Path filepath = Paths.get(project.getVersion(versionId).getArtefactsPath()).toAbsolutePath().normalize();
		Resource resource = new UrlResource(filepath.toUri());

		if (resource.exists()) {
			return resource;
		} else {
			throw new ResourceNotFoundException();
		}
	}
}
