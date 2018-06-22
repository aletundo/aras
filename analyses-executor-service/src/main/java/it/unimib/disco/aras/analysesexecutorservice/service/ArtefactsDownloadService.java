package it.unimib.disco.aras.analysesexecutorservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Service
@Slf4j
public class ArtefactsDownloadService {

	@Autowired
	private WebClient.Builder webClientBuilder;

	public Mono<Resource> downloadArtefacts(String projectId, String versionId) {
		Mono<Resource> artefacts = webClientBuilder.build().get()
				.uri("http://projects-service/projects/{projectId}/versions/{versionId}/artefacts", projectId, versionId)
				.retrieve()
				.bodyToMono(Resource.class)
				.log();
		log.info("Downloading artefacts of project {} version {}", projectId, versionId);
		return artefacts;
	}
}
