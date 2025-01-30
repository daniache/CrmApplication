package org.manmet.crmapplication.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.util.Set;

@Entity
@Data
public class Opportunity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String dealName;
    private String Description;
    private Double dealValue;

    @Enumerated(EnumType.STRING)
    private OpportunityStatus status;

    @OneToMany(mappedBy = "opportunity")
    private Set<OpportunitiesPipeline> opportunitiesPipelines;

    // Lead as foreign key referencing Customers table
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lead_id",nullable = true)  // FK to Customers table (since leads are potential customers)
    @JsonBackReference(value = "lead-opportunity")  // Child reference
    private Lead lead;  // This will reference the Customers entity (leads are potential customers)

    // Association with Lead for potential customers
    @ManyToOne
    @JoinColumn(name = "customer_id", nullable = true)
    @JsonBackReference(value = "customer-opportunity")  // Reference for customers
    private Customers customer;
    public enum OpportunityStatus {
        NEW, IN_PROGRESS, CLOSED_WON, CLOSED_LOST;
    }

}
