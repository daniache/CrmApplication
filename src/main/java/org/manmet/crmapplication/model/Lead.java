package org.manmet.crmapplication.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Entity
@Data
@Table(name = "leads")
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
@ToString
public class Lead {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String leadName;
    private String email;
    private int phone;

    @ManyToOne
    @JoinColumn(name = "customer_id", nullable = true)  // Nullable, allowing null values
    @JsonBackReference(value = "customer-lead")
    private Customers customer;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "pipeline_stage_id", nullable = false)
    @JsonManagedReference // This makes sure that PipelineStage is serialized when accessing Lead
    private PipelineStage pipelineStage;

    private String leadSource;

    private String status;

    // Lead to Opportunities relationship
    @OneToMany(mappedBy = "lead", fetch = FetchType.EAGER)
    @JsonManagedReference(value = "lead-opportunity") // This ensures that Opportunities are serialized when accessing Lead
    private Set<Opportunity> opportunities;
}
