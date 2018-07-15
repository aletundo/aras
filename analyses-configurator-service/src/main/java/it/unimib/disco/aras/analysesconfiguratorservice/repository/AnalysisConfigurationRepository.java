/*
 * 
 */
package it.unimib.disco.aras.analysesconfiguratorservice.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import it.unimib.disco.aras.analysesconfiguratorservice.entity.AnalysisConfiguration;

/**
 * The Interface AnalysisConfigurationRepository.
 */
@RepositoryRestResource(collectionResourceRel = "configurations", path = "configurations")
public interface AnalysisConfigurationRepository extends MongoRepository<AnalysisConfiguration, String> {
	
	/**
	 * Find by project id.
	 *
	 * @param projectId
	 *            the project id
	 * @return the optional
	 */
	public Optional<List<AnalysisConfiguration>> findByProjectId(String projectId);
	
	/**
	 * Find by project id and version id.
	 *
	 * @param projectId
	 *            the project id
	 * @param versionId
	 *            the version id
	 * @return the optional
	 */
	public Optional<AnalysisConfiguration> findByProjectIdAndVersionId(String projectId, String versionId);
}
