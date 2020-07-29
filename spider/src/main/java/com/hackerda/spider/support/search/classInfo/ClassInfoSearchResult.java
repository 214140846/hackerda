/**
  * Copyright 2019 bejson.com 
  */
package com.hackerda.spider.support.search.classInfo;

import lombok.Data;

/**
 * Auto-generated: 2019-10-08 13:30:58
 *
 * @author bejson.com (i@bejson.com)
 * @website http://www.bejson.com/java2pojo/
 */
@Data
public class ClassInfoSearchResult {

    private String admissionDate;
    private String admissionGrade;
    private String className;
    private String departmentName;
    private String departmentNum;
    private String executiveEducationPlanName;
    private Id id;
    private String subjectName;
    private String subjectNum;
    private String zxjxjhh;

    @Data
    public static class Id{
        private String classNum;
        private String executiveEducationPlanNumber;
    }

}