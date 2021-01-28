package com.hackerda.platform.domain.grade;

import com.hackerda.platform.domain.student.StudentUserBO;

import java.util.List;

public interface GradeFetchService {

    /**
     * 这里的设计没有区分出本学期的成绩和以往的成绩，
     * 对外提供的是一个一致性的结果，抓取成功，或抓取失败
     * 抓取成功会有对应的数据，失败的情况映射为对应的错误码
     *
     *
     * @param student 需要抓取的学生
     * @return 抓取结果的总览
     */
    GradeOverviewBO getAllByStudent(StudentUserBO student);

}
