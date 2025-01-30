package org.manmet.crmapplication.controller;

import org.manmet.crmapplication.model.Customers;
import org.manmet.crmapplication.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/customers")
public class CustomerController {

    @Autowired
    private CustomerService customerService;
    
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('add_customer')")
    @PostMapping("/create")
    public ResponseEntity<Customers> createCustomer(@RequestBody Customers customer) {
        try {
            Customers createdCustomer = customerService.createCustomer(customer);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdCustomer);
        } catch (Exception e) {
            System.err.println("Error creating customer: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER') or hasAuthority('view_customers')")
    @GetMapping("/all")
    public ResponseEntity<List<Customers>> findAll() {
        try {
            List<Customers> customers = customerService.findAll();
            return ResponseEntity.ok(customers);
        } catch (Exception e) {
            System.err.println("Error fetching customers: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PreAuthorize("hasRole('ADMIN') or hasAuthority('view_customers')")
    @GetMapping("/{id}")
    public ResponseEntity<Customers> findById(@PathVariable Long id) {
        try {
            Customers customer = customerService.findById(id);
            if (customer == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
            return ResponseEntity.ok(customer);
        } catch (Exception e) {
            System.err.println("Error fetching customer by ID: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('edit_customer')")
    @PutMapping("/{id}/update")
    public ResponseEntity<Customers> updateCustomer(@PathVariable Long id, @RequestBody Customers customer) {
        try {
            Customers updatedCustomer = customerService.updateCustomer(id, customer);
            return ResponseEntity.ok(updatedCustomer);
        } catch (Exception e) {
            System.err.println("Error updating customer: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('delete_customer')")
    @DeleteMapping("/{id}/delete")
    public ResponseEntity<Void> deleteCustomer(@PathVariable Long id) {
        try {
            customerService.deleteCustomer(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            System.err.println("Error deleting customer: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
