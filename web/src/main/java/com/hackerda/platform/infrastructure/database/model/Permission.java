package com.hackerda.platform.infrastructure.database.model;

import java.io.Serializable;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * permission
 * @author 
 */
@Data
@Accessors(chain = true)
public class Permission implements Serializable {
    private Integer id;

    private String permissionName;

    private String permissionCode;

    private String priority;

    private static final long serialVersionUID = 1L;
}