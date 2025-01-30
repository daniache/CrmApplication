package org.manmet.crmapplication.service;

import org.manmet.crmapplication.model.Permissions;
import org.manmet.crmapplication.model.Roles;
import org.manmet.crmapplication.repository.PermissionsRepository;
import org.manmet.crmapplication.repository.RolesRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class RolesPermissionService {

    private final PermissionsRepository permissionsRepository;
    private final RolesRepository rolesRepository;
    private static final Logger logger = LoggerFactory.getLogger(RolesPermissionService.class);

    @Autowired
    public RolesPermissionService(PermissionsRepository permissionsRepository, RolesRepository rolesRepository) {
        this.permissionsRepository = permissionsRepository;
        this.rolesRepository = rolesRepository;
    }


    // Method to assign permissions to a role by their IDs
    public ResponseEntity<String> addPermissionToRole(Long roleId, Set<Long> permissionIds) {
        // Fetch the role from the database
        Roles role = rolesRepository.findById(roleId)
                .orElseThrow(() -> new RuntimeException("Role not found"));

        // Fetch the permissions based on the provided IDs
        Set<Permissions> permissions = new HashSet<>(permissionsRepository.findAllById(permissionIds));

        logger.info("Role: {}", role);
        logger.info("Permissions: {}", permissions);
        if (permissions.isEmpty()) {
            return new ResponseEntity<>("Permissions not found", HttpStatus.NOT_FOUND);
        }

        // Update the role with the new permissions
        role.getPermissions().addAll(permissions); // Add new permissions to the existing set
        rolesRepository.save(role); // This will also update the role_permissions table

        return new ResponseEntity<>("Permissions added to role successfully", HttpStatus.OK);
    }

    // Method to get permissions for a role
    public ResponseEntity<Set<Permissions>> getPermissionsForRole(String roleName) {
        Roles role = rolesRepository.findByRoleName(roleName)
                .orElseThrow(() -> new RuntimeException("Role not found"));

        Set<Permissions> permissions = role.getPermissions();
        return new ResponseEntity<>(permissions, HttpStatus.OK);
    }
}
