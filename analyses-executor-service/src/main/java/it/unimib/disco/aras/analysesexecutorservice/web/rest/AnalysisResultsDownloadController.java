package it.unimib.disco.aras.analysesexecutorservice.web.rest;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import it.unimib.disco.aras.analysesexecutorservice.service.AnalysisResultsService;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/results/{analysisResultsId}")
@Slf4j
public class AnalysisResultsController {
	
	@Autowired
	private AnalysisResultsService analysisResultsService;
	
	@GetMapping(value = "/download", produces = "application/zip")
	public ResponseEntity<?> downloadResults(@PathVariable("analysisResultsId") String analysisResultsId) {
		try {
			Resource results = analysisResultsService.loadResults(analysisResultsId);
			HttpHeaders headers = new HttpHeaders();
			headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + results.getFilename());
			return ResponseEntity.ok().headers(headers).body(new InputStreamResource(results.getInputStream()));
		} catch (IOException e) {
			log.error(e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}
}
