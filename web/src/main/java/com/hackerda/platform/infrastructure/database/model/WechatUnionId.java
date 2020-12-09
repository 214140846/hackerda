package com.hackerda.platform.infrastructure.database.model;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * wechat_union_id
 * @author 
 */
@Data
public class WechatUnionId implements Serializable {
    private Integer id;

    private String unionId;

    private String appId;

    private String openId;

    private Date gmtCreate;

    private Date gmtModify;

    private static final long serialVersionUID = 1L;
}