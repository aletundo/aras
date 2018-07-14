package it.unimib.disco.aras.testsuite.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import it.unimib.disco.aras.testsuite.stream.consumer.Consumer;
import it.unimib.disco.aras.testsuite.stream.message.ReportMessage;
import it.unimib.disco.aras.testsuite.stream.message.ReportStatus;
import it.unimib.disco.aras.testsuite.web.rest.client.ReportsServiceClient;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ReportService {

	@Autowired
	private Consumer<ReportMessage> reportConsumer;

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private ReportsServiceClient reportsServiceClient;

	public void verifyReportGenerated(String analysisId) throws IOException {
		ResponseEntity<String> response = reportsServiceClient.getReportsByAnalysisId(analysisId);
		JsonNode body = objectMapper.readTree(response.getBody());
		HttpStatus status = response.getStatusCode();

		assertThat(status).isEqualTo(HttpStatus.OK);
		assertThat(body.at("/_embedded/reports").isArray()).isEqualTo(true);
		assertThat(body.at("/_embedded/reports/0/reportPath").isMissingNode()).isEqualTo(false);
		assertThat(body.at("/_embedded/reports/0/reportPath").isNull()).isEqualTo(false);
		assertThat(body.at("/_embedded/reports/0/analysisId").textValue()).isEqualTo(analysisId);
	}
	
	public void verifyReportGeneratedMessage(ReportStatus reportStatusToCheck) throws InterruptedException {
		reportConsumer.getLatch().await(1, TimeUnit.MINUTES);
		assertThat(reportConsumer.getLatch().getCount()).isEqualTo(0);
		assertThat(reportConsumer.getMessages().get(0).getReportStatus()).isEqualTo(reportStatusToCheck);
	}
}
