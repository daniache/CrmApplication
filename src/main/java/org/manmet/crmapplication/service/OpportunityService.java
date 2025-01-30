package org.manmet.crmapplication.service;

import org.manmet.crmapplication.model.Opportunity;
import org.manmet.crmapplication.repository.OpportunityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
@Service
public class OpportunityService {

    private final OpportunityRepository opportunityRepository;

    @Autowired
    public OpportunityService(OpportunityRepository opportunityRepository) {
        this.opportunityRepository = opportunityRepository;
    }
    public List<Opportunity> findAll() {
        return opportunityRepository.findAll();
    }
    public Optional<Opportunity> findById(Long id) {
        return opportunityRepository.findById(id);
    }

    public Opportunity save(Opportunity opportunity) {
        return opportunityRepository.save(opportunity);
    }
    public void deleteById(Long id) {
        opportunityRepository.deleteById(id);
    }
}
