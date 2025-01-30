package org.manmet.crmapplication.service;

import org.manmet.crmapplication.customexceptionhandler.ResourceNotFoundException;
import org.manmet.crmapplication.model.Lead;
import org.manmet.crmapplication.model.PipelineStage;
import org.manmet.crmapplication.repository.LeadRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class LeadService {
    private final LeadRepository leadRepository;
    private final PipelineStageService pipelineStageService;

    @Autowired
    public LeadService(LeadRepository leadRepository, PipelineStageService pipelineStageService) {
        this.leadRepository = leadRepository;
        this.pipelineStageService = pipelineStageService;
    }

    public List<Lead> findAll() {
        return leadRepository.findAll();
    }

    public Lead findById(Long id) {
        return leadRepository.findById(id).orElse(null);  // Returns null if no lead is found
    }

    public Lead save(Lead lead) {
        return leadRepository.save(lead);
    }

    public Lead updateLead(Long id, Lead leadDetails) {
        Lead existingLead = findById(id);
        existingLead.setLeadName(leadDetails.getLeadName());
        existingLead.setEmail(leadDetails.getEmail());
        existingLead.setPhone(leadDetails.getPhone());
        existingLead.setLeadSource(leadDetails.getLeadSource());

        // If status is updated, set the corresponding pipeline stage
        if (leadDetails.getStatus() != null && !leadDetails.getStatus().equals(existingLead.getStatus())) {
            PipelineStage correspondingStage = pipelineStageService.getPipelineStageByStatus(leadDetails.getStatus());
            existingLead.setPipelineStage(correspondingStage);
        }

        // If pipeline stage is updated, set the corresponding status
        if (leadDetails.getPipelineStage() != null && !leadDetails.getPipelineStage().equals(existingLead.getPipelineStage())) {
            String correspondingStatus = pipelineStageService.getStatusByPipelineStage(leadDetails.getPipelineStage());
            existingLead.setStatus(correspondingStatus);
        }

        return leadRepository.save(existingLead);
    }

    public void deleteLead(Long id) {
        leadRepository.deleteById(id);
    }

    public Lead updateLeadPipelineStage(Long id, String status) {
        // Fetch the lead by its ID
        Lead lead = leadRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Lead not found"));

        // Map status to pipeline stage using the getPipelineStageByStatus method
        PipelineStage pipelineStage = pipelineStageService.getPipelineStageByStatus(status);

        // Update the lead's pipeline stage
        lead.setPipelineStage(pipelineStage);

        lead.setStatus(status);  // Ensure status and pipeline stage are in sync

        // Save and return the updated lead
        return leadRepository.save(lead);
    }
}
