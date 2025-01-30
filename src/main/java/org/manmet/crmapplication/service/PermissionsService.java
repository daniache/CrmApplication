package org.manmet.crmapplication.service;


import org.manmet.crmapplication.model.Permissions;
import org.manmet.crmapplication.repository.PermissionsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PermissionsService {

    private final PermissionsRepository permissionsRepository;

    @Autowired
    public PermissionsService(PermissionsRepository permissionsRepository) {
        this.permissionsRepository = permissionsRepository;
    }

    // CRUD operations for Permissions

    // Find all permissions
    public List<Permissions> findAll() {
        return permissionsRepository.findAll();
    }

    // Find permission by ID
    public Optional<Permissions> findById(Long id) {
        return permissionsRepository.findById(id);
    }

    // Save or update a permission
    public Permissions save(Permissions permissions) {
        return permissionsRepository.save(permissions);
    }

    // Delete a permission by ID
    public void deleteById(Long id) {
        permissionsRepository.deleteById(id);
    }
}
