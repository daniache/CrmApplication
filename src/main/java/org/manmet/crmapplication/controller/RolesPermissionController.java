package org.manmet.crmapplication.controller;

import org.manmet.crmapplication.dto.RolePermissionDTO;
import org.manmet.crmapplication.service.RolesPermissionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/assign-permissions")
public class RolesPermissionController {

    private final RolesPermissionService rolesPermissionService;

    public RolesPermissionController(RolesPermissionService rolesPermissionService) {
        this.rolesPermissionService = rolesPermissionService;
    }

    @PostMapping
    public ResponseEntity<String> assignPermissions(@RequestBody RolePermissionDTO rolePermissionDTO) {
        try {
            return rolesPermissionService.addPermissionToRole(rolePermissionDTO.getRoleId(), rolePermissionDTO.getPermissionIds());
        } catch (Exception e) {
            // Log the exception for debugging purposes
            e.printStackTrace();
            return new ResponseEntity<>("Internal server error", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
