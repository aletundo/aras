package it.unimib.disco.aras.notificationsservice.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import it.unimib.disco.aras.notificationsservice.entity.ReportNotification;

/**
 * The Interface ReportNotificationRepository.
 */
@RepositoryRestResource(collectionResourceRel = "report-notifications", path = "report-notifications")
public interface ReportNotificationRepository extends MongoRepository<ReportNotification, String> {
	
	/**
	 * Find by analysis id.
	 *
	 * @param analysisId
	 *            the analysis id
	 * @return the list
	 */
	public List<ReportNotification> findByAnalysisId(String analysisId);
	
	/**
	 * Find by report id.
	 *
	 * @param reportId
	 *            the report id
	 * @return the optional
	 */
	public Optional<ReportNotification> findByReportId(String reportId);
}
