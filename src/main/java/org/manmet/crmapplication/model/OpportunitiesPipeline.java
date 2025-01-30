package org.manmet.crmapplication.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class OpportunitiesPipeline {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "opportunity_id", nullable = false)
    private Opportunity opportunity;

    @ManyToOne
    @JoinColumn (name = "pipeline_stage_id", nullable = false)
    private PipelineStage stage;
}
