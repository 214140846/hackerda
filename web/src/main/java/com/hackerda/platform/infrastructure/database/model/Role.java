package com.hackerda.platform.infrastructure.database.model;

import java.io.Serializable;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * role
 * @author 
 */
@Data
@Accessors(chain = true)
public class Role implements Serializable {
    private Integer id;

    private String name;

    private String code;

    private Integer priority;

    private static final long serialVersionUID = 1L;
}