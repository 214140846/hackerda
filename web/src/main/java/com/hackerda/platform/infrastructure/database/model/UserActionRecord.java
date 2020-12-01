package com.hackerda.platform.infrastructure.database.model;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * user_action_record
 * @author 
 */
@Data
public class UserActionRecord implements Serializable {
    private Long id;

    private String operator;

    private Integer userAction;

    private Integer actionTarget;

    private String targetId;

    private String actionValue;

    private Date operateTime;

    private Date gmtCreate;

    private Date gmtModify;

    private static final long serialVersionUID = 1L;
}