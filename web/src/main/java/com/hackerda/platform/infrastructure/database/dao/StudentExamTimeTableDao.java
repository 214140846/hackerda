package com.hackerda.platform.infrastructure.database.dao;

import com.hackerda.platform.infrastructure.database.mapper.StudentExamTimetableMapper;
import com.hackerda.platform.infrastructure.database.model.StudentExamTimetable;
import com.hackerda.platform.infrastructure.database.model.StudentExamTimetableExample;
import com.hackerda.platform.infrastructure.database.model.example.ExamTimetable;
import com.hackerda.platform.utils.DateUtils;
import com.hackerda.platform.utils.Term;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

import javax.annotation.Resource;

@Service
public class StudentExamTimeTableDao {
    @Resource
    private StudentExamTimetableMapper studentExamTimetableMapper;
	
    public void insertSelective(StudentExamTimetable record){
        studentExamTimetableMapper.insertSelective(record);
    }
    
    public StudentExamTimetable selectByAccountAndExamTimetable(String account,ExamTimetable exam){
		StudentExamTimetableExample example = new StudentExamTimetableExample();
		example.createCriteria().andAccountEqualTo(account).andExamTimetableIdEqualTo(exam.getId());
		List<StudentExamTimetable> list = studentExamTimetableMapper.selectByExample(example);
		if(!CollectionUtils.isEmpty(list)) {
			return list.get(0);
		}
		return null;
    }

    public void insert(String account,ExamTimetable exam){
    	Term term=DateUtils.getCurrentSchoolTime().getTerm();
    	StudentExamTimetable studentExamTimetable=new StudentExamTimetable();
    	studentExamTimetable.setAccount(account)
    	.setExamTimetableId(exam.getId())
    	.setTermOrder(term.getOrder())
    	.setTermYear(String.valueOf(term.getEndYear()));
    	studentExamTimetableMapper.insert(studentExamTimetable);
    }  
    public int insertBatch(List<StudentExamTimetable> list) {
    	return studentExamTimetableMapper.batchInsert(list);
    }

}
