package it.unimib.disco.aras.projectsservice.entity;

import java.util.Date;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@NoArgsConstructor
@Document(collection="projects")
public class Project {
	@Id
	private String id;
	private String name;
	private String description;
	private List<String> tags;
	private String repositoryUrl;
	private Date createdAt;
	@LastModifiedDate
	private Date updatedAt;
	private List<Version> versions;
	
	public Version getVersion(@NonNull String versionId) {
		for(Version v : this.versions) {
			if(versionId.equals(v.getId())) {
				return v;
			}
		}
		return null;
	}
}