package org.manmet.crmapplication.service;

import org.manmet.crmapplication.customexceptionhandler.ResourceNotFoundException;
import org.manmet.crmapplication.model.PipelineStage;
import org.manmet.crmapplication.repository.PipelineStageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PipelineStageService {

    @Autowired
    private PipelineStageRepository pipelineStageRepository;

    public List<PipelineStage> getAllStages() {
        return pipelineStageRepository.findAll();
    }


    public PipelineStage findById(Long id) {
        return pipelineStageRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Stage not found"));
    }
    // Method to map status to pipeline stage
    public PipelineStage getPipelineStageByStatus(String status) {
        switch (status.toUpperCase()) {
            case "NEW":
                return findById(1L);
            case "CONTACTED":
                return findById(2L);
            case "QUALIFIED":
                return findById(3L);
            case "PROPOSING":
                return findById(4L);
            case "NEGOTIATING":
                return findById(5L);
            case "WON":
                return findById(6L);
            case "ONBOARDED":
                return findById(7L);
            case "LOST":
                return null;  // Lost leads don't correspond to a pipeline stage
            default:
                throw new IllegalArgumentException("Invalid status: " + status);
        }
    }

    // Method to map pipeline stage to status
    public String getStatusByPipelineStage(PipelineStage stage) {
        if (stage == null) {
            return "LOST";  // Return LOST if there's no pipeline stage
        }

        switch (stage.getId().intValue()) {
            case 1:
                return "NEW";
            case 2:
                return "CONTACTED";
            case 3:
                return "QUALIFIED";
            case 4:
                return "PROPOSING";
            case 5:
                return "NEGOTIATING";
            case 6:
                return "WON";
            case 7:
                return "ONBOARDED";
            default:
                throw new IllegalArgumentException("Unknown pipeline stage: " + stage.getId());
        }
    }
    public PipelineStage updateStage(Long id, PipelineStage stageDetails) {
        PipelineStage stage = pipelineStageRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Stage not found"));
        stage.setPipelineStageName(stageDetails.getPipelineStageName());
        stage.setDescription(stageDetails.getDescription());
        stage.setStageOrder(stageDetails.getStageOrder());
        stage.setActive(stageDetails.isActive());
        return pipelineStageRepository.save(stage);
    }

    public void deleteStage(Long id) {
        PipelineStage stage = pipelineStageRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Stage not found"));
        pipelineStageRepository.delete(stage);
    }
}

