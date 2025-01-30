package org.manmet.crmapplication.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;

import java.util.Set;

@Entity
@Data
public class PipelineStage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String pipelineStageName;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private int stageOrder;

    @Column(nullable = false)
    private boolean active;


    @OneToMany(mappedBy = "stage")
    @JsonBackReference
    private Set<OpportunitiesPipeline> opportunitiesPipelines;
}
