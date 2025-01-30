package org.manmet.crmapplication.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.Set;

@Entity
@Table(name = "customers")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
@ToString
public class Customers {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false) // This column cannot be null
    @NotBlank
    private String business_name;
    private String address;
    private String email;
    private String phone;
    private String industry;

    @OneToMany(mappedBy = "customer")
    @JsonManagedReference(value = "customer-lead")  // Manages the relationship with Lead
    private Set<Lead> leads;

    @OneToMany(mappedBy = "customer")
    @JsonManagedReference(value = "customer-opportunity")  // Parent reference
    private Set<Opportunity> opportunities;
}
