package it.unimib.disco.aras.reportsservice.web.rest;

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

import it.unimib.disco.aras.reportsservice.service.ReportService;
import lombok.extern.slf4j.Slf4j;

/**
 * The Class ReportDownloadController.
 */
@RestController
@RequestMapping("/reports/{reportId}")

/** The Constant log. */
@Slf4j
public class ReportDownloadController {
	
	/** The report service. */
	@Autowired
	private ReportService reportService;
	
	/**
	 * Download report.
	 *
	 * @param reportId
	 *            the report id
	 * @return the response entity
	 */
	@GetMapping(value = "/download", produces = "application/pdf")
	public ResponseEntity<?> downloadReport(@PathVariable("reportId") String reportId) {
		try {
			Resource results = reportService.loadReport(reportId);
			HttpHeaders headers = new HttpHeaders();
			headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + results.getFilename());
			return ResponseEntity.ok().headers(headers).body(new InputStreamResource(results.getInputStream()));
		} catch (IOException e) {
			log.error(e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}
}
