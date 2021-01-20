package com.hackerda.platform.domain.course.timetable;

import com.hackerda.platform.domain.course.CourseBO;
import com.hackerda.platform.domain.time.Term;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.NotNull;

import java.util.Date;

@Data
@Accessors(chain = true)
public class CourseTimetableBO implements Comparable<CourseTimetableBO>{

    @EqualsAndHashCode.Exclude
    private Integer id;

    private int index;

    private CourseBO courseBO;

    private String roomName;

    private String roomNumber;

    private String campusName;

    private Integer continuingSession;

    private String courseId;

    @EqualsAndHashCode.Exclude
    private String attendClassTeacher;

    @EqualsAndHashCode.Exclude
    private Integer studentCount;

    private Integer classDay;

    private Integer classOrder;

    private Integer startWeek;

    private Integer endWeek;

    private String classInSchoolWeek;

    private String courseSequenceNumber;

    private String weekDescription;

    @EqualsAndHashCode.Exclude
    private Integer classDistinct;

    private String termYear;

    private Integer termOrder;

    @EqualsAndHashCode.Exclude
    private Date gmtCreate;

    public Term getTerm() {
        return new Term(termYear, termOrder);
    }

    /**
     * 星期一第一节：1
     * 星期一第二节：3
     * 星期二第一节：13
     * @return 课程排序的序号
     */
    public int getOrder() {
        return (classDay-1) * 12 + classOrder;
    }

    /**
     * 本周是否上课
     * @param schoolWeek 学校的教学周
     * @return 上课则返回true
     */
    public boolean isHoldWeek(int schoolWeek) {
        if (schoolWeek < 1 || schoolWeek > classInSchoolWeek.length()) {
            return false;
        }

        return classInSchoolWeek.charAt(schoolWeek - 1) == '1';
    }


    @Override
    public int compareTo(@NotNull CourseTimetableBO o) {

        if(this.getOrder() != o.getOrder()) {
            return Integer.compare(this.getOrder(), o.getOrder());
        }

        return Integer.compare(this.startWeek, o.startWeek);

    }
}
