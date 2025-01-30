package org.manmet.crmapplication.repository;

import org.manmet.crmapplication.model.PipelineStage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PipelineStageRepository extends JpaRepository<PipelineStage, Long> {
}
