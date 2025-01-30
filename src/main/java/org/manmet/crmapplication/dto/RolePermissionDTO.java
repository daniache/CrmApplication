package org.manmet.crmapplication.dto;

import lombok.Data;

import java.util.Set;
@Data
public class RolePermissionDTO {
    private Long roleId;
    private Set<Long> permissionIds;
}
