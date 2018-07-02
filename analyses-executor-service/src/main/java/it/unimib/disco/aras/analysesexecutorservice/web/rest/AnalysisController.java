package it.unimib.disco.aras.analysesexecutorservice.web.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import it.unimib.disco.aras.analysesexecutorservice.service.AnalysisJobService;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/analysis/{analysisId}")
@Slf4j
public class AnalysisController {

	@Autowired
	private AnalysisJobService analysisJobService;

	@PatchMapping("/pause")
	public ResponseEntity<?> pauseAnalysis(@PathVariable String analysisId) {
		try {
			analysisJobService.pauseJob(analysisId);
			return ResponseEntity.ok().build();
		} catch (RuntimeException e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}

	}

	@PatchMapping("/resume")
	public ResponseEntity<?> resume(@PathVariable String analysisId) {
		try {
			analysisJobService.resumeJob(analysisId);
			return ResponseEntity.ok().build();
		} catch (RuntimeException e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}
}
