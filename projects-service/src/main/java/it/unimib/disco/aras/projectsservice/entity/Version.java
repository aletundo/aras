package it.unimib.disco.aras.projectsservice.entity;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Document
public class Version {
	@Id
	private String id;
	private String description;
	private Date createdAt;
	private Date updatedAt;
	private String versionTag;
	private String artefactsPath;
}
