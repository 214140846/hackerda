package com.hackerda.spider.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author junrong.chen
 * @date 2019/7/17
 */
@EqualsAndHashCode(callSuper = false)
@Data
public class UrpRequestException extends UrpException {
    private String url;
    private int code;
    private String message;

    public UrpRequestException(String url, int code, String message){
        super("url: " + url + " code: " + code + " cause: " + message);
        this.url = url;
        this.code = code;
        this.message = message;
    }

    public UrpRequestException(String url, int code, Throwable e){
        super("url: " + url + " code: " + code, e);
        this.url = url;
        this.code = code;
    }
}
