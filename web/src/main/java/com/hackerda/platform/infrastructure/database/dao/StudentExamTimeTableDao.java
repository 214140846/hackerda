package com.hackerda.platform.infrastructure.database.dao;

import com.hackerda.platform.infrastructure.database.mapper.StudentExamTimetableMapper;
import com.hackerda.platform.infrastructure.database.model.StudentExamTimetable;
import com.hackerda.platform.infrastructure.database.model.StudentExamTimetableExample;
import com.hackerda.platform.infrastructure.database.model.example.ExamTimetable;
import com.hackerda.platform.utils.DateUtils;
import com.hackerda.platform.utils.Term;

import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

@Service
public class StudentExamTimeTableDao {
    @Resource
    private StudentExamTimetableMapper studentExamTimetableMapper;
	
    private Term term=DateUtils.getCurrentSchoolTime().getTerm();


    public void insertSelective(StudentExamTimetable record){
        studentExamTimetableMapper.insertSelective(record);
    }
    
    public List<StudentExamTimetable> selectByAccountAndExamTimetable(String account,ExamTimetable exam){
		StudentExamTimetableExample example = new StudentExamTimetableExample();
		example.createCriteria().andAccountEqualTo(account).andExamTimetableIdEqualTo(exam.getId());
		List<StudentExamTimetable> list = studentExamTimetableMapper.selectByExample(example);
		return list;
    }

    public void insert(String account,ExamTimetable exam){
    	StudentExamTimetable studentExamTimetable=new StudentExamTimetable();
    	studentExamTimetable.setAccount(account)
    	.setExamTimetableId(exam.getId())
    	.setTermOrder(term.getOrder())
    	.setTermYear(String.valueOf(term.getEndYear()));
    	studentExamTimetableMapper.insert(studentExamTimetable);
    }

}
