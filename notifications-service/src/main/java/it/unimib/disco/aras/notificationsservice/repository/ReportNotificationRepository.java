package it.unimib.disco.aras.notificationsservice.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import it.unimib.disco.aras.notificationsservice.entity.ReportNotification;

@RepositoryRestResource(collectionResourceRel = "report-notifications", path = "report-notifications")
public interface ReportNotificationRepository extends MongoRepository<ReportNotification, String> {

}
