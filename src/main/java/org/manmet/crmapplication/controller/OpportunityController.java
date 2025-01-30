package org.manmet.crmapplication.controller;

import org.manmet.crmapplication.model.Opportunity;
import org.manmet.crmapplication.service.OpportunityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/opportunities")
public class OpportunityController {

    @Autowired
    private OpportunityService opportunityService;

    @PostMapping("/create")
    @PreAuthorize("hasAuthority('add_opportunity')")
    public ResponseEntity<Opportunity> createOpportunity(@RequestBody Opportunity opportunity) {
        try {
            Opportunity createdOpportunity = opportunityService.save(opportunity);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdOpportunity);
        } catch (Exception e) {
            System.err.println("Error creating opportunity: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/all")
    @PreAuthorize("hasAuthority('view_opportunities')")
    public ResponseEntity<List<Opportunity>> findAll() {
        try {
            List<Opportunity> opportunities = opportunityService.findAll();
            return ResponseEntity.ok(opportunities);
        } catch (Exception e) {
            System.err.println("Error fetching opportunities: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Opportunity> findById(@PathVariable Long id) {
        try {
            Optional<Opportunity> opportunity = opportunityService.findById(id);
            if (opportunity.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
            return ResponseEntity.ok(opportunity.get());
        } catch (Exception e) {
            System.err.println("Error fetching opportunity by ID: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PutMapping("/{id}/update")
    @PreAuthorize("hasAuthority('edit_opportunity')")
    public ResponseEntity<Opportunity> updateOpportunity(@PathVariable Long id, @RequestBody Opportunity opportunity) {
        try {
            Opportunity updatedOpportunity = opportunityService.save(opportunity);
            return ResponseEntity.ok(updatedOpportunity);
        } catch (Exception e) {
            System.err.println("Error updating opportunity: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @DeleteMapping("/{id}/delete")
    @PreAuthorize("hasAuthority('delete_opportunity')")
    public ResponseEntity<Void> deleteOpportunity(@PathVariable Long id) {
        try {
            opportunityService.deleteById(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            System.err.println("Error deleting opportunity: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
