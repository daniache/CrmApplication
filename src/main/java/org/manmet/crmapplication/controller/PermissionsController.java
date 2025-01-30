package org.manmet.crmapplication.controller;

import org.manmet.crmapplication.model.Permissions;
import org.manmet.crmapplication.service.PermissionsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/permissions")
public class PermissionsController {

    private final PermissionsService permissionsService;

    @Autowired
    public PermissionsController(PermissionsService permissionsService) {
        this.permissionsService = permissionsService;
    }

    @PostMapping
    public ResponseEntity<Permissions> createPermission(@RequestBody Permissions permission) {
        try {
            Permissions createdPermission = permissionsService.save(permission);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdPermission);
        } catch (Exception e) {
            System.err.println("Error creating permission: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping
    public ResponseEntity<List<Permissions>> findAll() {
        try {
            List<Permissions> permissions = permissionsService.findAll();
            return ResponseEntity.ok(permissions);
        } catch (Exception e) {
            System.err.println("Error fetching permissions: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Permissions> findById(@PathVariable Long id) {
        try {
            Optional<Permissions> permission = permissionsService.findById(id);
            return permission.map(ResponseEntity::ok)
                    .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body(null));
        } catch (Exception e) {
            System.err.println("Error fetching permission by ID: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Permissions> updatePermission(@PathVariable Long id, @RequestBody Permissions permissionDetails) {
        try {
            Optional<Permissions> existingPermissionOptional = permissionsService.findById(id);

            if (existingPermissionOptional.isPresent()) {
                Permissions existingPermission = existingPermissionOptional.get();

                // Update the permission details
                existingPermission.setPermissionName(permissionDetails.getPermissionName());
                existingPermission.setDescription(permissionDetails.getDescription());
                // Add any other fields you want to update here

                Permissions updatedPermission = permissionsService.save(existingPermission);
                return ResponseEntity.ok(updatedPermission);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
        } catch (Exception e) {
            System.err.println("Error updating permission: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePermission(@PathVariable Long id) {
        try {
            Optional<Permissions> existingPermissionOptional = permissionsService.findById(id);
            if (existingPermissionOptional.isPresent()) {
                permissionsService.deleteById(id);
                return ResponseEntity.noContent().build();
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
        } catch (Exception e) {
            System.err.println("Error deleting permission: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
