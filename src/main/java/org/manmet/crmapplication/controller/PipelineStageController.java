package org.manmet.crmapplication.controller;

import org.manmet.crmapplication.model.PipelineStage;
import org.manmet.crmapplication.service.PipelineStageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pipeline-stages")
public class PipelineStageController {

    @Autowired
    private PipelineStageService pipelineStageService;


    @GetMapping
    public ResponseEntity<List<PipelineStage>> getAllStages() {
        try {
            List<PipelineStage> stages = pipelineStageService.getAllStages();
            return ResponseEntity.ok(stages);
        } catch (Exception e) {
            System.err.println("Error fetching pipeline stages: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<PipelineStage> getPipelineStageById(@PathVariable Long id) {
        try {
            PipelineStage stage = pipelineStageService.findById(id);
            if (stage == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
            return ResponseEntity.ok(stage);
        } catch (Exception e) {
            System.err.println("Error fetching pipeline stage by ID: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<PipelineStage> updatePipelineStage(@PathVariable Long id, @RequestBody PipelineStage pipelineStage) {
        try {
            PipelineStage updatedStage = pipelineStageService.updateStage(id, pipelineStage);
            if (updatedStage == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
            return ResponseEntity.ok(updatedStage);
        } catch (Exception e) {
            System.err.println("Error updating pipeline stage: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePipelineStage(@PathVariable Long id) {
        try {
            pipelineStageService.deleteStage(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            System.err.println("Error deleting pipeline stage: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
