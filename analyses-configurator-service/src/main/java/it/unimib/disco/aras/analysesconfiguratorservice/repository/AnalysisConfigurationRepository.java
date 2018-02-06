package it.unimib.disco.aras.analysesconfiguratorservice.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import it.unimib.disco.aras.analysesconfiguratorservice.entity.AnalysisConfiguration;

@RepositoryRestResource(collectionResourceRel = "configurations", path = "configurations")
public interface AnalysisConfigurationRepository extends PagingAndSortingRepository<AnalysisConfiguration, UUID> {
    List<AnalysisConfiguration> findByVersionId(@Param("versionId") UUID versionId);
}
