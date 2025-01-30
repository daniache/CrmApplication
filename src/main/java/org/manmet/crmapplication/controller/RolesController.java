package org.manmet.crmapplication.controller;

import org.manmet.crmapplication.model.Permissions;
import org.manmet.crmapplication.model.Roles;
import org.manmet.crmapplication.service.RolesPermissionService;
import org.manmet.crmapplication.service.RolesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@RestController
@RequestMapping("/api/roles")
public class RolesController {

    private final RolesService rolesService;
    private final RolesPermissionService rolesPermissionService;

    @Autowired
    public RolesController(RolesService rolesService, RolesPermissionService rolesPermissionService) {
        this.rolesService = rolesService;
        this.rolesPermissionService = rolesPermissionService;
    }

    // CRUD operations for roles using RolesService
    @GetMapping
    public ResponseEntity<List<Roles>> findAllRoles() {
        List<Roles> roles = rolesService.findAll();
        //roles.forEach(role -> System.out.println(role.getPermissions()));
        return ResponseEntity.ok(roles);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Roles> findRoleById(@PathVariable Long id) {
        Optional<Roles> role = rolesService.findById(id);
        return role.map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @PostMapping
    public ResponseEntity<Roles> saveRole(@RequestBody Roles role) {
        Roles savedRole = rolesService.save(role);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedRole);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRole(@PathVariable Long id) {
        rolesService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    // Role-Permission management using RolesPermissionService
    @GetMapping("/{roleName}/permissions")
    public ResponseEntity<Set<Permissions>> getPermissionsForRole(@PathVariable String roleName) {
        return rolesPermissionService.getPermissionsForRole(roleName);
    }

    @PostMapping("/roles/{roleId}/permissions")
    public ResponseEntity<String> addPermissionsToRole(
            @PathVariable Long roleId,
            @RequestBody Set<Long> permissionIds) {
        return rolesPermissionService.addPermissionToRole(roleId, permissionIds);
    }


    /*@PostMapping("/setup")
    public ResponseEntity<Void> setupRoles() {
        return rolesPermissionService.setupRoles();
    }*/
}
