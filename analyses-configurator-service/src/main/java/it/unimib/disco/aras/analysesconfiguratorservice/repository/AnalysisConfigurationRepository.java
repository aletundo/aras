package it.unimib.disco.aras.analysesconfiguratorservice.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import it.unimib.disco.aras.analysesconfiguratorservice.entity.AnalysisConfiguration;

@RepositoryRestResource(collectionResourceRel = "configurations", path = "configurations")
public interface AnalysisConfigurationRepository extends MongoRepository<AnalysisConfiguration, String> {
	public Optional<List<AnalysisConfiguration>> findByProjectId(String projectId);
	public Optional<AnalysisConfiguration> findByProjectIdAndVersionId(String projectId, String versionId);
}
