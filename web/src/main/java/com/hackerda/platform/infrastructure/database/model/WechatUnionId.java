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

    private Byte subscribe;

    private Date gmtCreate;

    private Date gmtModify;

    private static final long serialVersionUID = 1L;


    public boolean isSubscribe_() {
        return subscribe == (byte) 1;
    }

    public void setSubscribe (boolean subscribe) {
        this.subscribe = subscribe ? (byte) 1 : (byte) 0;
    }
}