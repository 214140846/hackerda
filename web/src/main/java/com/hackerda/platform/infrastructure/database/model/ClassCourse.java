package com.hackerda.platform.infrastructure.database.model;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class ClassCourse {
    private Integer id;

    private String courseId;

    private String courseOrder;

    private String classId;

    private String termYear;

    private Integer termOrder;

}