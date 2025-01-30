package org.manmet.crmapplication.service;

import org.manmet.crmapplication.model.Customers;
import org.manmet.crmapplication.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerService {

    @Autowired
    private CustomerRepository customerRepository;

    public Customers createCustomer(Customers customer){
        return customerRepository.save(customer);
    }

    public List<Customers> findAll() {
        return customerRepository.findAll();
    }


    public Customers findById(Long id) {
        return customerRepository.findById(id).orElse(null);
    }

    public Customers updateCustomer(Long id, Customers customer) {
        customer.setId(id);
        return customerRepository.save(customer);
    }


    public void deleteCustomer(Long id) {
        customerRepository.deleteById(id);
    }
}
