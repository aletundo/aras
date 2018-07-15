package it.unimib.disco.aras.projectsservice.web.rest;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import it.unimib.disco.aras.projectsservice.entity.Project;
import it.unimib.disco.aras.projectsservice.repository.ProjectRepository;
import it.unimib.disco.aras.projectsservice.service.ArtefactStorageService;
import lombok.extern.slf4j.Slf4j;

/**
 * The Class ProjectController.
 */
@RestController
@RequestMapping("/projects/{projectId}/versions/{versionId}")

/** The Constant log. */
@Slf4j
public class ProjectController {

	/** The artefact storage service. */
	@Autowired
	private ArtefactStorageService artefactStorageService;

	/** The project repository. */
	@Autowired
	private ProjectRepository projectRepository;

	/**
	 * Upload version artefacts.
	 *
	 * @param projectId
	 *            the project id
	 * @param versionId
	 *            the version id
	 * @param artefact
	 *            the artefact
	 * @return the response entity
	 */
	@PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<?> uploadVersionArtefacts(@PathVariable("projectId") String projectId,
			@PathVariable("versionId") String versionId, @RequestParam("artefact") MultipartFile artefact) {

		if (projectRepository.findByVersionId(versionId).isPresent() && !artefact.isEmpty()
				&& "application/zip".equals(artefact.getContentType())) {
			try {
				Project savedProject = artefactStorageService.store(projectId, versionId, artefact);
				return ResponseEntity.ok(savedProject);
			} catch (IOException e) {
				log.error(e.getMessage());
				return ResponseEntity.badRequest().build();
			}
		} else {
			return ResponseEntity.badRequest().build();
		}
	}

	/**
	 * Download version artefacts.
	 *
	 * @param projectId
	 *            the project id
	 * @param versionId
	 *            the version id
	 * @return the response entity
	 */
	@GetMapping(value = "/artefacts", produces = "application/zip")
	public ResponseEntity<?> downloadVersionArtefacts(@PathVariable("projectId") String projectId,
			@PathVariable("versionId") String versionId) {
		try {
			Resource artefacts = artefactStorageService.load(versionId);
			HttpHeaders headers = new HttpHeaders();
			headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + artefacts.getFilename());
			return ResponseEntity.ok().headers(headers).body(new InputStreamResource(artefacts.getInputStream()));
		} catch (IOException e) {
			log.error(e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}
}