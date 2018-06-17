package it.unimib.disco.aras.analysesconfiguratorservice.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import it.unimib.disco.aras.analysesconfiguratorservice.entity.AnalysisConfiguration;

@RepositoryRestResource(collectionResourceRel = "configurations", path = "configurations")
public interface AnalysisConfigurationRepository extends MongoRepository<AnalysisConfiguration, String> {

}
