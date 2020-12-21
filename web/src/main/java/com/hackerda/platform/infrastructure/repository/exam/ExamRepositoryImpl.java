package com.hackerda.platform.infrastructure.repository.exam;

import com.hackerda.platform.domain.exam.ExamRepository;
import com.hackerda.platform.domain.exam.ExamTimetableBO;
import com.hackerda.platform.domain.student.StudentAccount;
import com.hackerda.platform.infrastructure.database.mapper.ExamTimetableMapper;
import com.hackerda.platform.infrastructure.database.mapper.StudentExamTimetableMapper;
import com.hackerda.platform.infrastructure.database.model.StudentExamTimetableExample;
import com.hackerda.platform.infrastructure.database.model.example.ExamTimetable;
import com.hackerda.platform.infrastructure.database.model.example.ExamTimetableExample;
import com.hackerda.platform.utils.Term;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

@Repository
public class ExamRepositoryImpl implements ExamRepository {

    @Autowired
    private ExamTimetableMapper examTimetableMapper;
    @Autowired
    private StudentExamTimetableMapper studentExamTimetableMapper;

    @Override
    public List<ExamTimetableBO> findTimetable(String courseName, Term term) {

        ExamTimetableExample example = new ExamTimetableExample();
        example.createCriteria()
                .andCourseNameEqualTo(courseName)
                .andTermOrderEqualTo(String.valueOf(term.getOrder()))
                .andTermYearEqualTo(term.getTermYear());

        return examTimetableMapper.selectByExample(example).stream()
                .map(this::adapt)
                .collect(Collectors.toList());
    }


    public ExamTimetableBO adapt(ExamTimetable examTimetable) {

        ExamTimetableBO bo = new ExamTimetableBO();

        bo.setId(examTimetable.getId());
        bo.setCourseName(examTimetable.getCourseName());
        bo.setExamDate(examTimetable.getExamDate());
        bo.setStartTime(examTimetable.getStartTime());
        bo.setEndTime(examTimetable.getEndTime());
        bo.setRoomName(examTimetable.getRoomName());
        bo.setName(examTimetable.getName());
        bo.setSchoolWeek(examTimetable.getSchoolWeek());
        bo.setDay(examTimetable.getDay());
        bo.setTerm(new Term(examTimetable.getTermYear()+"-"+examTimetable.getTermOrder()));

        StudentExamTimetableExample example = new StudentExamTimetableExample();
        example.createCriteria().andExamTimetableIdEqualTo(examTimetable.getId());

        List<StudentAccount> accountList = studentExamTimetableMapper.selectByExample(example).stream()
                .map(x -> new StudentAccount(x.getAccount()))
                .collect(Collectors.toList());

        bo.setStudentAccountList(accountList);
        return bo;
    }




}
