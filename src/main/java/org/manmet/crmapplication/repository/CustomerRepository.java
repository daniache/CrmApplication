package org.manmet.crmapplication.repository;

import org.manmet.crmapplication.model.Customers;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends JpaRepository<Customers, Long>,
        JpaSpecificationExecutor<Customers> {
}
