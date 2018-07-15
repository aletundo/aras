package it.unimib.disco.aras.analysesexecutorservice.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import it.unimib.disco.aras.analysesexecutorservice.entity.AnalysisResults;

/**
 * The Interface AnalysisResultsRepository.
 */
@RepositoryRestResource(collectionResourceRel = "results", path = "results")
public interface AnalysisResultsRepository extends MongoRepository<AnalysisResults, String> {
	
	/**
	 * Find by analysis id.
	 *
	 * @param analysisId
	 *            the analysis id
	 * @return the optional
	 */
	public Optional<AnalysisResults> findByAnalysisId(String analysisId);
}
