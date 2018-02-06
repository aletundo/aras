package it.unimib.disco.aras.analysesconfiguratorservice.entity;

import java.util.Date;
import java.util.UUID;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
public class AnalysisConfiguration {
    
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private UUID id;
    
    private UUID projectId;
    
    private UUID versionId;
    
    private Date startTime;
}
