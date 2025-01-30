package org.manmet.crmapplication.controller;

import org.manmet.crmapplication.model.Lead;
import org.manmet.crmapplication.model.PipelineStage;
import org.manmet.crmapplication.service.LeadService;
import org.manmet.crmapplication.service.PipelineStageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/leads")
public class LeadController {

    @Autowired
    private LeadService leadService;
    @Autowired
    private PipelineStageService pipelineStageService;

    @PreAuthorize("hasAuthority('view_leads')")
    @GetMapping
    public ResponseEntity<List<Lead>> getAllLeads() {
        try {
            List<Lead> leads = leadService.findAll();
            return ResponseEntity.ok(leads);
        } catch (Exception e) {
            System.err.println("Error fetching leads: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
    @PreAuthorize("hasAuthority('add_lead')")
    @PostMapping("/create")
    public ResponseEntity<Lead> createLead(@RequestBody Lead lead) {
        try {
            // Fetch and assign the selected pipeline stage
            PipelineStage pipelineStage = pipelineStageService.findById(lead.getPipelineStage().getId());
            if (pipelineStage == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);  // Handle invalid stage
            }

            lead.setPipelineStage(pipelineStage);

            // Save the lead
            Lead createdLead = leadService.save(lead);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdLead);
        } catch (Exception e) {
            System.err.println("Error creating lead: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Lead> getLeadById(@PathVariable Long id) {
        try {
            Lead lead = leadService.findById(id);  // Assume the findById method now returns Lead directly instead of Optional<Lead>
            if (lead == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
            return ResponseEntity.ok(lead);
        } catch (Exception e) {
            System.err.println("Error fetching lead by ID: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PreAuthorize("hasAuthority('edit_lead')")
    @PutMapping("/{id}/update")
    public ResponseEntity<Lead> updateLead(@PathVariable Long id, @RequestBody Lead leadDetails) {
        try {
            // Save the updated lead
            Lead updatedLead = leadService.updateLead(id, leadDetails);
            return ResponseEntity.ok(updatedLead);
        } catch (Exception e) {
            System.err.println("Error updating lead: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }


    @PutMapping("/{id}/updatePipelineStage")
    public ResponseEntity<Lead> updateLeadPipelineStage(@PathVariable Long id, @RequestBody Map<String, String> updateDetails) {
        try {
            String newStatus = updateDetails.get("status");
            Lead updatedLead = leadService.updateLeadPipelineStage(id, newStatus);
            return ResponseEntity.ok(updatedLead);
        } catch (Exception e) {
            System.err.println("Error updating lead pipeline stage: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('delete_lead')")
    @DeleteMapping("/{id}/delete")
    public ResponseEntity<Void> deleteLead(@PathVariable Long id) {
        try {
            leadService.deleteLead(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            System.err.println("Error deleting lead: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
