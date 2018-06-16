package it.unimib.disco.aras.notificationsservice.stream.message;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor(staticName="build")
@RequiredArgsConstructor
public class ReportMessage {
	private String id;
	private String reportId;
	private ReportStatus reportStatus;
}
