package com.hackerda.platform.domain.grade;

import com.hackerda.platform.domain.course.CourseBO;
import com.hackerda.platform.pojo.Course;
import com.hackerda.platform.utils.DateUtils;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.StringUtils;

import java.util.Date;

@Data
public class GradeBO {
    private CourseBO course;

    @EqualsAndHashCode.Exclude
    private Integer id;

    @EqualsAndHashCode.Exclude
    private Integer examId = 0;

    private Integer account;

    private Double score;

    private Double credit;

    @EqualsAndHashCode.Exclude
    private Double gradePoint;

    private String levelName;

    private String levelPoint;

    @EqualsAndHashCode.Exclude
    private Integer rank;

    private String courseName;

    private String courseNumber;

    private String courseOrder;

    private String coursePropertyCode;

    private String coursePropertyName;

    private String examTypeCode;

    private String examTypeName;

    private Integer studyHour;

    private String operateTime;

    private String operator;

    private String examTime;

    private String unpassedReasonCode;

    private String unpassedReasonExplain;

    private String remark;

    private String replaceCourseNumber;

    private String retakeCourseMark;

    private String retakecourseModeCode;

    private String retakeCourseModeExplain;

    private String standardPoint;

    private String termYear;

    private Integer termOrder;

    private boolean update = false;

    private boolean newGrade = false;

    @EqualsAndHashCode.Exclude
    private Date gmtCreate;
    @EqualsAndHashCode.Exclude
    private Date gmtModify;


    /**
     * 是否为选修课
     * @return
     */
    public boolean isOptional(){
        return getCoursePropertyCode().equals("003") || getCoursePropertyCode().equals("005");
    }

    /**
     * 是否为选修课
     * @return
     */
    public boolean hasScore(){
        return getScore() != -1;
    }

    public double getCourseCredit() {
        String creditStr = getCourse().getCredit();
        if(StringUtils.isNotEmpty(creditStr)){
            return Double.parseDouble(creditStr);
        }
        return 0;
    }

    /**
     * 学分绩点
     * @return
     */
    public double getCreditGradePoint() {
        return getGradePoint() * getCourseCredit();
    }


    public Date getOperateTime(){
        if (this.operateTime.length() == 12) {
            this.operateTime = this.operateTime + "00";
        }
        return DateUtils.localDateToDate(this.operateTime, DateUtils.PATTERN_WITHOUT_SPILT);

    }

    public String getOperateTimeStr() {
        if (this.operateTime.length() == 12) {
            this.operateTime = this.operateTime + "00";
        }
        return operateTime;

    }
}