package it.unimib.disco.aras.reportsservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Service
@Slf4j
public class AnalysisResultsDownloadService {
	@Autowired
	private WebClient.Builder webClientBuilder;

	public Mono<Resource> downloadAnalysisResults(String downloadUriString) {
		Mono<Resource> analysisResults = webClientBuilder.build().get()
				.uri("http://analyses-executor-service" + downloadUriString)
				.retrieve()
				.bodyToMono(Resource.class)
				.log();
		log.info("Downloading analysis results at {}", downloadUriString);
		return analysisResults;
	}
}