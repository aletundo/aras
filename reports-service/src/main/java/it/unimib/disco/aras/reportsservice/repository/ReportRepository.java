package it.unimib.disco.aras.reportsservice.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import it.unimib.disco.aras.reportsservice.entity.Report;

@RepositoryRestResource(collectionResourceRel = "reports", path = "reports")
public interface ReportRepository extends MongoRepository<Report, String> {
	public List<Report> findByAnalysisId(String analysisId);
}
