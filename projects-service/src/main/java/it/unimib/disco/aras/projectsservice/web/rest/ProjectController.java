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

import it.unimib.disco.aras.projectsservice.service.ArtefactStorageService;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/projects/{projectId}/versions/{versionId}")
@Slf4j
public class ProjectController{

	@Autowired
	private ArtefactStorageService artefactStorageService;

	@PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<?> uploadVersionArtefacts(@PathVariable("projectId") String projectId,
			@PathVariable("versionId") String versionId, @RequestParam("artefact") MultipartFile artefact) {
		if (!artefact.isEmpty()) {
			try {
				artefactStorageService.store(projectId, versionId, artefact);
				return ResponseEntity.ok(null);
			} catch (IOException e) {
				log.error(e.getMessage());
				return ResponseEntity.badRequest().build();
			}
		} else {
			return ResponseEntity.badRequest().build();
		}
	}

	@GetMapping(value = "/artefacts", produces = "application/zip")
	public ResponseEntity<?> downloadVersionArtefacts(@PathVariable("projectId") String projectId,
			@PathVariable("versionId") String versionId) {
		try {
			Resource artefacts = artefactStorageService.load(projectId, versionId);
			HttpHeaders headers = new HttpHeaders();
			headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + artefacts.getFilename());
			return ResponseEntity.ok().headers(headers).body(new InputStreamResource(artefacts.getInputStream()));
		} catch (IOException e) {
			log.error(e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}
}