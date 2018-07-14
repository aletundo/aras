package it.unimib.disco.aras.notificationsservice.service;

import java.sql.Date;
import java.time.Instant;

import javax.mail.MessagingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.unimib.disco.aras.notificationsservice.entity.NotificationType;
import it.unimib.disco.aras.notificationsservice.entity.ReportNotification;
import it.unimib.disco.aras.notificationsservice.entity.StatusNotification;
import it.unimib.disco.aras.notificationsservice.repository.ReportNotificationRepository;
import it.unimib.disco.aras.notificationsservice.repository.StatusNotificationRepository;
import it.unimib.disco.aras.notificationsservice.stream.message.AnalysisMessage;
import it.unimib.disco.aras.notificationsservice.stream.message.NotificationMessage;
import it.unimib.disco.aras.notificationsservice.stream.message.ReportMessage;
import it.unimib.disco.aras.notificationsservice.stream.message.ReportStatus;
import it.unimib.disco.aras.notificationsservice.stream.producer.Producer;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class NotificationService {

	@Autowired
	private Producer<NotificationMessage> notificationProducer;

	@Autowired
	private StatusNotificationRepository statusNotificationRepository;
	
	@Autowired
	private ReportNotificationRepository reportNotificationRepository;

	@Autowired
	private EmailService emailService;
	
	@Autowired
	private ReportsDownloadService reportsDownloadService;

	public void sendAnalysisStatusNotification(AnalysisMessage analysis) {
		// TODO:
		// - get subscribers
		// - get subscribers' channels
		// - run a dedicated job for each channel (eg. QuartzJob)
		emailService.sendSimpleMessage(System.getenv("NOTIFICATIONS_TO_ADDRESS_DEV"),
				"[ARAS] Analysis status notification",
				"The analysis with id " + analysis.getId() + " is on status " + analysis.getStatus());
		StatusNotification notification = StatusNotification.builder().analysisStatus(analysis.getStatus())
				.analysisId(analysis.getId()).createdAt(Date.from(Instant.now()))
				.notificationType(NotificationType.STATUS).build();
		notification = statusNotificationRepository.save(notification);
		notificationProducer
				.dispatch(NotificationMessage.builder().id(notification.getId()).type(NotificationType.STATUS).build());
	}
	
	public void sendReportNotification(ReportMessage report) {
		// TODO:
		// - get subscribers
		// - get subscribers' channels
		// - run a dedicated job for each channel (eg. QuartzJob)
		switch (report.getReportStatus()) {
		case GENERATED:
			sendReportGeneratedNotification(report.getId(), report.getAnalysisId(), report.getDownloadUriString());
			break;
		case FAILED:
			sendReportFailedNotification(report.getAnalysisId());
			break;
		default:
			break;
		}
		
		
	}
	
	private void sendReportFailedNotification(String analysisId) {
		emailService.sendSimpleMessage(System.getenv("NOTIFICATIONS_TO_ADDRESS_DEV"),
				"[ARAS] Failed report generation",
				"The report of the analysis with id " + analysisId + " has not been generated successfully :( ");
		ReportNotification notification = ReportNotification.builder().createdAt(Date.from(Instant.now()))
				.analysisId(analysisId)
				.reportStatus(ReportStatus.FAILED)
				.notificationType(NotificationType.REPORT).build();
		notification = reportNotificationRepository.save(notification);
		notificationProducer
				.dispatch(NotificationMessage.builder().id(notification.getId()).type(NotificationType.REPORT).build());
		
	}

	private void sendReportGeneratedNotification(String reportId, String analysisId, String downloadUriString) {
		reportsDownloadService.downloadReport(downloadUriString).subscribe(pdfReport -> {
			try {
				emailService.sendAttachmentMessage(System.getenv("NOTIFICATIONS_TO_ADDRESS_DEV"),
						"[ARAS] A new report has been generated",
						"The report for the analysis with id " + analysisId + " has been generated.", pdfReport);
				ReportNotification notification = ReportNotification.builder().createdAt(Date.from(Instant.now()))
						.reportId(reportId)
						.analysisId(analysisId)
						.reportStatus(ReportStatus.GENERATED)
						.notificationType(NotificationType.REPORT).build();
				
				notification = reportNotificationRepository.save(notification);
				notificationProducer
						.dispatch(NotificationMessage.builder().id(notification.getId()).type(NotificationType.REPORT).build());
			} catch (MessagingException e) {
				log.error("Failed to send report notification due to {}", e.getMessage());
			}
		});
	}
}
