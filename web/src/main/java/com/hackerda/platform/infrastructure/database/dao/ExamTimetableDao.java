package com.hackerda.platform.infrastructure.database.dao;

import com.hackerda.platform.infrastructure.database.mapper.ExamTimetableMapper;
import com.hackerda.platform.infrastructure.database.model.Exam;
import com.hackerda.platform.infrastructure.database.model.example.ExamTimetable;
import com.hackerda.platform.infrastructure.database.model.example.ExamTimetableExample;

import com.hackerda.platform.utils.DateUtils;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

import java.util.List;


@Service
public class ExamTimetableDao {
    @Resource
    private ExamTimetableMapper examTimetableMapper;
    
    @Autowired
    private StudentExamTimeTableDao studentExamTimeTableDao;    
 
    //通过查询出来的exam对象插入数据
    public void insertByExam(ExamTimetable examTimetable) {
	    examTimetableMapper.insert(examTimetable);
    }
    
    //通过exam查询数据库中是否存在
    public ExamTimetable selectByExam(ExamTimetable examTimetable) {
		ExamTimetableExample example = new ExamTimetableExample();
		example.createCriteria().andRoomNameEqualTo(examTimetable.getRoomName())
		.andCourseNameEqualTo(examTimetable.getCourseName())
		.andExamDateEqualTo(examTimetable.getExamDate());
		List<ExamTimetable> list= examTimetableMapper.selectByExample(example);
		if(!CollectionUtils.isEmpty(list)) {
			return list.get(0);
		}
		return null;		
    }
    
    public int batchInsert(List<ExamTimetable> list) {
    	return examTimetableMapper.batchInsert(list);
    }
    
    public List<ExamTimetable> getExamByAccount(String account){
    	return examTimetableMapper.selectByAccount(account);
    }
}
