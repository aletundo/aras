package it.unimib.disco.aras.notificationsservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

/**
 * The Class ReportsDownloadService.
 */
@Service

/** The Constant log. */
@Slf4j
public class ReportsDownloadService {
	
	/** The web client builder. */
	@Autowired
	private WebClient.Builder webClientBuilder;

	/**
	 * Download report.
	 *
	 * @param downloadUriString
	 *            the download uri string
	 * @return the mono
	 */
	public Mono<Resource> downloadReport(String downloadUriString) {
		Mono<Resource> report = webClientBuilder.build().get()
				.uri("http://reports-service" + downloadUriString)
				.retrieve()
				.bodyToMono(Resource.class)
				.log();
		log.info("Downloading report at {}", downloadUriString);
		return report;
	}
}