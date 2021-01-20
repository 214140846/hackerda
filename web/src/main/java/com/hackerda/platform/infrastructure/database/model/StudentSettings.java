package com.hackerda.platform.infrastructure.database.model;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Date;
import java.util.Map;
import java.util.stream.Collectors;

import com.hackerda.platform.domain.student.Switch;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * student_settings
 * @author 
 */
@Slf4j
@Data
public class StudentSettings implements Serializable {
    private Integer id;

    private String account;

    @Switch(name = "grade")
    private Boolean gradePushSwitch;

    @Switch(name = "course")
    private Boolean coursePushSwitch;

    @Switch(name = "exam")
    private Boolean examPushSwitch;

    @Switch(name = "comment")
    private Boolean commentPushSwitch;

    private Date gmtCreate;

    private Date gmtModify;



    private static final long serialVersionUID = 1L;

}