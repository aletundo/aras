package it.unimib.disco.aras.projectsservice.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import it.unimib.disco.aras.projectsservice.entity.Project;

/**
 * The Interface ProjectRepository.
 */
@RepositoryRestResource(collectionResourceRel = "projects", path = "projects")
public interface ProjectRepository extends MongoRepository<Project, String> {
    
    /**
	 * Find by version id.
	 *
	 * @param versionId
	 *            the version id
	 * @return the optional
	 */
    @Query(value = "{ 'versions._id' : ?0 }")
	public Optional<Project> findByVersionId(String versionId);
}
