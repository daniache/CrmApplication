package org.manmet.crmapplication.service;

import org.manmet.crmapplication.model.Permissions;
import org.manmet.crmapplication.model.Roles;
import org.manmet.crmapplication.repository.RolesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class RolesService {

    private final RolesRepository rolesRepository;

    @Autowired
    public RolesService(RolesRepository roleRepository) {
        this.rolesRepository = roleRepository;
    }
    public List<Roles> findAll() {
        return rolesRepository.findAll();
    }
    public Optional<Roles> findById(Long id) {
        return rolesRepository.findById(id);
    }
    public Roles save(Roles roles) {
        return rolesRepository.save(roles);
    }
    public void deleteById(Long id) {
        rolesRepository.deleteById(id);
    }

}
