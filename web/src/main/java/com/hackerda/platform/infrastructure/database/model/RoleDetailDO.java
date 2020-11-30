package com.hackerda.platform.infrastructure.database.model;

import lombok.Data;

@Data
public class RoleDetailDO {

    private Integer roleId;
    private String roleName;
    private String roleCode;
    private Integer rolePriority;

    private Integer permissionId;
    private String permissionName;
    private String permissionCode;
    private Integer permissionPriority;

}
