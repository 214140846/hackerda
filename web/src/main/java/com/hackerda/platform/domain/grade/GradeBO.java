package com.hackerda.platform.domain.grade;

import com.hackerda.platform.domain.course.CourseBO;
import com.hackerda.platform.utils.DateUtils;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;

@Data
public class GradeBO {

    private CourseBO course;

    private Integer id;

    private Integer examId = 0;

    private Integer account;

    private Double score;

    private Double credit;

    private Double gradePoint;

    private String levelName;

    private String levelPoint;

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

    private String unPassedReasonCode;

    private String unPassedReasonExplain;

    private String remark;

    private String replaceCourseNumber;

    private String retakeCourseMark;

    private String retakeCourseModeCode;

    private String retakeCourseModeExplain;

    private String standardPoint;

    private String termYear;

    private Integer termOrder;

    private boolean update = false;

    private boolean scoreUpdate = false;

    private boolean newGrade = false;

    private boolean show = true;

    private Date gmtCreate;

    private Date gmtModify;


    /**
     * 是否为选修课
     * @return
     */
    public boolean isOptional(){
        return (getCoursePropertyCode().equals("003")
                ||
                getCoursePropertyCode().equals("005"))
                &&
                (getCourseOrder().equals("01")
                ||
                getCourseOrder().equals("02")) ;
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

    /**
     * 是否是今天更新的成绩
     * @return
     */
    public boolean isTodayUpdate() {

        Calendar today = Calendar.getInstance();
        today.setTime(new Date());

        LocalDate operateTime = getOperateTime().toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();

        Period period = Period.between(operateTime, LocalDate.now());
        return period.getDays() == 0 && period.getMonths() == 0 && period.getYears()==0;

    }


    public String getOperateTimeStr() {
        if (this.operateTime.length() == 12) {
            this.operateTime = this.operateTime + "00";
        }
        return operateTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        GradeBO gradeBO = (GradeBO) o;

        return new EqualsBuilder()
                .append(account, gradeBO.account)
                .append(score, gradeBO.score)
                .append(credit, gradeBO.credit)
                .append(gradePoint, gradeBO.gradePoint)
                .append(levelName, gradeBO.levelName)
                .append(levelPoint, gradeBO.levelPoint)
                .append(rank, gradeBO.rank)
                .append(courseName, gradeBO.courseName)
                .append(courseNumber, gradeBO.courseNumber)
                .append(courseOrder, gradeBO.courseOrder)
                .append(coursePropertyCode, gradeBO.coursePropertyCode)
                .append(coursePropertyName, gradeBO.coursePropertyName)
                .append(examTypeCode, gradeBO.examTypeCode)
                .append(examTypeName, gradeBO.examTypeName)
                .append(studyHour, gradeBO.studyHour)
                .append(operateTime, gradeBO.operateTime)
                .append(operator, gradeBO.operator)
                .append(examTime, gradeBO.examTime)
                .append(unPassedReasonCode, gradeBO.unPassedReasonCode)
                .append(unPassedReasonExplain, gradeBO.unPassedReasonExplain)
                .append(remark, gradeBO.remark)
                .append(replaceCourseNumber, gradeBO.replaceCourseNumber)
                .append(retakeCourseMark, gradeBO.retakeCourseMark)
                .append(retakeCourseModeCode, gradeBO.retakeCourseModeCode)
                .append(retakeCourseModeExplain, gradeBO.retakeCourseModeExplain)
                .append(standardPoint, gradeBO.standardPoint)
                .append(termYear, gradeBO.termYear)
                .append(termOrder, gradeBO.termOrder)
                .append(show, gradeBO.show)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(account)
                .append(score)
                .append(courseName)
                .append(courseNumber)
                .append(courseOrder)
                .append(termYear)
                .append(termOrder)
                .toHashCode();
    }
}
