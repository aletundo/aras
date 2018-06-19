package it.unimib.disco.aras.projectsservice.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import it.unimib.disco.aras.projectsservice.entity.Project;

@RepositoryRestResource(collectionResourceRel = "projects", path = "projects")
public interface ProjectRepository extends MongoRepository<Project, String> {
    @Query(value = "{ 'versions._id' : ?0 }")
	public Project findByVersionId(String versionId);
}
