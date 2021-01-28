package com.hackerda.platform.controller.vo;

import lombok.Data;
import lombok.NonNull;
import lombok.experimental.Accessors;

import java.util.Date;

@Data
@Accessors(chain = true)
public class GradeVO implements Comparable<GradeVO>{
    private CourseVO course;

    private Integer account;

    private Double score;

    private Double gradePoint;

    private Integer rank;

    private Date operateTime;

    private Date examTime;

    private String examTimeStr;

    private String standardPoint;

    private String termYear;

    private Integer termOrder;

    private Integer errorCode = 0;

    private String msg;

    private String coursePropertyCode;

    private String coursePropertyName;

    private boolean update = false;

    @Override
    public int compareTo(@NonNull GradeVO gradeVo) {
        if(gradeVo.getTermYear().compareTo(this.getTermYear()) == 0){
            if(gradeVo.getTermOrder().compareTo(this.getTermOrder()) == 0){
                return gradeVo.getOperateTime().compareTo(this.getOperateTime());
            }else {
                return gradeVo.getTermOrder().compareTo(this.getTermOrder());
            }
        }else {
            return gradeVo.getTermYear().compareTo(this.getTermYear());
        }

    }
}
