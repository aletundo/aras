package it.unimib.disco.aras.notificationsservice.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import it.unimib.disco.aras.notificationsservice.entity.StatusNotification;

/**
 * The Interface StatusNotificationRepository.
 */
@RepositoryRestResource(collectionResourceRel = "status-notifications", path = "status-notifications")
public interface StatusNotificationRepository extends MongoRepository<StatusNotification, String> {
	
	/**
	 * Find by analysis id.
	 *
	 * @param analysisId
	 *            the analysis id
	 * @return the list
	 */
	public List<StatusNotification> findByAnalysisId(String analysisId);
}
