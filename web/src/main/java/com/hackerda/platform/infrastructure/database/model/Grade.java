package com.hackerda.platform.infrastructure.database.model;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.apache.commons.lang3.StringUtils;

/**
 * grade
 * @author 
 */
@Data
@Accessors(chain = true)
public class Grade implements Serializable {

    private static final long serialVersionUID = 8938192019053638674L;

    @EqualsAndHashCode.Exclude
    private Integer id;

    /**
     * 对应的考试编号
     */
    @EqualsAndHashCode.Exclude
    private Integer examId;

    /**
     * 学号
     */
    private Integer account;

    /**
     * 分数
     */
    private Double score;

    /**
     * 学分
     */
    private Double credit;

    /**
     * 根据gpa计算规则换算的数值
     */
    @EqualsAndHashCode.Exclude
    private Double gradePoint;

    /**
     * 成绩等级名称 如“优秀”，“良好”等
     */
    private String levelName;

    /**
     * 对应成绩等级的分数
     */
    private String levelPoint;

    /**
     * 排名
     */
    @EqualsAndHashCode.Exclude
    private Integer rank;

    /**
     * 课程名
     */
    private String courseName;

    /**
     * 课程号
     */
    private String courseNumber;

    /**
     * 课序号，课程号相同时作为标识
     */
    private String courseOrder;

    /**
     * 课程类型代码
     */
    private String coursePropertyCode;

    /**
     * 课程类型名称
     */
    private String coursePropertyName;

    /**
     * 考试类型代码
     */
    private String examTypeCode;

    /**
     * 考试类名名称
     */
    private String examTypeName;

    /**
     * 学时
     */
    private Integer studyHour;

    /**
     * 操作时间
     */
    private String operateTime;

    /**
     * 操作人，老师的id或者root
     */
    private String operator;

    /**
     * 考试时间
     */
    private String examTime;

    /**
     * 未通过原因编号
     */
    private String unpassedReasonCode;

    /**
     * 未通过原因解释
     */
    private String unpassedReasonExplain;

    /**
     * 备注
     */
    private String remark;

    /**
     * 替换课程编号
     */
    private String replaceCourseNumber;

    /**
     * 重修课程标识
     */
    private String retakeCourseMark;

    /**
     * 重修课程模式编号
     */
    private String retakecourseModeCode;

    /**
     * 重修课程模式解释
     */
    private String retakeCourseModeExplain;

    /**
     * 标准分数
     */
    private String standardPoint;

    private Boolean isShow;

    private String termYear;

    private Integer termOrder;

    @EqualsAndHashCode.Exclude
    private Date gmtCreate;
    @EqualsAndHashCode.Exclude
    private Date gmtModify;

    private boolean update = false;

    public String getLevelName(){
        if(StringUtils.isEmpty(this.levelName)){
            return "";
        }
        return this.levelName;

    }

    public String getExamTypeCode() {
        if(StringUtils.isEmpty(this.examTypeCode)){
            return "";
        }
        return examTypeCode;
    }

    public String getUnpassedReasonCode() {
        if(StringUtils.isEmpty(this.unpassedReasonCode)){
            return "";
        }
        return unpassedReasonCode;
    }

    public String getUnpassedReasonExplain() {
        if(StringUtils.isEmpty(this.unpassedReasonExplain)){
            return "";
        }
        return unpassedReasonExplain;
    }

    public String getRemark() {
        if(StringUtils.isEmpty(this.remark)){
            return "";
        }
        return remark;
    }

    public String getReplaceCourseNumber() {
        if(StringUtils.isEmpty(this.replaceCourseNumber)){
            return "";
        }
        return replaceCourseNumber;
    }

    public String getRetakeCourseMark() {
        if(StringUtils.isEmpty(this.retakeCourseMark)){
            return "";
        }
        return retakeCourseMark;
    }

    public String getRetakecourseModeCode() {
        if(StringUtils.isEmpty(this.retakecourseModeCode)){
            return "";
        }
        return retakecourseModeCode;
    }

    public String getRetakeCourseModeExplain() {
        if(StringUtils.isEmpty(this.retakeCourseModeExplain)){
            return "";
        }
        return retakeCourseModeExplain;
    }

    public String getStandardPoint() {
        if(StringUtils.isEmpty(this.standardPoint)){
            return "";
        }
        return standardPoint;
    }

    public Grade setGradePoint(Double gradePoint) {
        if (gradePoint == 0 && this.score != -1) {
            double v = this.getScore() - 60;
            if (v < 0) {
                this.gradePoint = 0.0;
            } else {
                this.gradePoint = 1 + v / 10;
            }

        }else {
            this.gradePoint = gradePoint;
        }
        return this;
    }

}