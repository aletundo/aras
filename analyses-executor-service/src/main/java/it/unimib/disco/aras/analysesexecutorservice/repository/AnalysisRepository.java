package it.unimib.disco.aras.analysesexecutorservice.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import it.unimib.disco.aras.analysesexecutorservice.entity.Analysis;

@RepositoryRestResource(collectionResourceRel = "analyses", path = "analyses")
public interface AnalysisRepository extends MongoRepository<Analysis, String> {

}
