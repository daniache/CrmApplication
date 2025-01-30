package org.manmet.crmapplication.repository;

import org.manmet.crmapplication.model.Opportunity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OpportunityRepository extends JpaRepository<Opportunity, Long> {
    // Additional custom query methods (if needed) can be defined here
}

