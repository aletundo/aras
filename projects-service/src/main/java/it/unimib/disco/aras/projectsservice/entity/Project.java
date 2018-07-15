package it.unimib.disco.aras.projectsservice.entity;

import java.util.Date;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

/* (non-Javadoc)
 * @see java.lang.Object#toString()
 */
@Data

/**
 * Instantiates a new project.
 */
@NoArgsConstructor
@Document(collection="projects")
public class Project {
	
	/** The id. */
	@Id
	private String id;
	
	/** The name. */
	private String name;
	
	/** The description. */
	private String description;
	
	/** The tags. */
	private List<String> tags;
	
	/** The repository url. */
	private String repositoryUrl;
	
	/** The created at. */
	private Date createdAt;
	
	/** The updated at. */
	@LastModifiedDate
	private Date updatedAt;
	
	/** The versions. */
	private List<Version> versions;
	
	/**
	 * Gets the version.
	 *
	 * @param versionId
	 *            the version id
	 * @return the version
	 */
	public Version getVersion(@NonNull String versionId) {
		for(Version v : this.versions) {
			if(versionId.equals(v.getId())) {
				return v;
			}
		}
		return null;
	}
}
