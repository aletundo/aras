package it.unimib.disco.aras.analysesexecutorservice.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import it.unimib.disco.aras.analysesexecutorservice.entity.AnalysisResults;

@RepositoryRestResource(collectionResourceRel = "results", path = "results")
public interface AnalysisResultsRepository extends MongoRepository<AnalysisResults, String> {
	public Optional<AnalysisResults> findByAnalysisId(String analysisId);
}
