package it.unimib.disco.aras.projectsservice.entity;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;
import lombok.NoArgsConstructor;

/* (non-Javadoc)
 * @see java.lang.Object#toString()
 */
@Data

/**
 * Instantiates a new version.
 */
@NoArgsConstructor
@Document
public class Version {
	
	/** The id. */
	@Id
	private String id;
	
	/** The name. */
	private String name;
	
	/** The description. */
	private String description;
	
	/** The created at. */
	private Date createdAt;
	
	/** The updated at. */
	private Date updatedAt;
	
	/** The version tag. */
	private String versionTag;
	
	/** The artefacts path. */
	private String artefactsPath;
}
