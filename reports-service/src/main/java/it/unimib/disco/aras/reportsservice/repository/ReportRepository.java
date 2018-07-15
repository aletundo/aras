package it.unimib.disco.aras.reportsservice.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import it.unimib.disco.aras.reportsservice.entity.Report;

/**
 * The Interface ReportRepository.
 */
@RepositoryRestResource(collectionResourceRel = "reports", path = "reports")
public interface ReportRepository extends MongoRepository<Report, String> {
	
	/**
	 * Find by analysis id.
	 *
	 * @param analysisId
	 *            the analysis id
	 * @return the list
	 */
	public List<Report> findByAnalysisId(String analysisId);
}
