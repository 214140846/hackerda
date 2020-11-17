package com.hackerda.platform.infrastructure.database.dao;

import com.hackerda.platform.infrastructure.database.mapper.ExamTimetableMapper;
import com.hackerda.platform.infrastructure.database.model.Exam;
import com.hackerda.platform.infrastructure.database.model.StudentExamTimetable;
import com.hackerda.platform.infrastructure.database.model.example.ExamTimetable;
import com.hackerda.platform.infrastructure.database.model.example.ExamTimetableExample;
import com.hackerda.platform.utils.Term;

import lombok.extern.slf4j.Slf4j;

import com.hackerda.platform.utils.DateUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.unit.DataUnit;

import javax.annotation.Resource;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static com.hackerda.platform.infrastructure.database.mapper.ExamTimetableDynamicSqlSupport.*;
import static com.hackerda.platform.infrastructure.database.mapper.StudentExamTimetableDynamicSqlSupport.studentExamTimetable;
import static org.mybatis.dynamic.sql.SqlBuilder.*;
@Slf4j
@Service
public class ExamTimetableDao {
    @Resource
    private ExamTimetableMapper examTimetableMapper;
    
    @Autowired
    private StudentExamTimeTableDao studentExamTimeTableDao;    
 
    //通过查询出来的exam对象插入数据
    public void insertByExam(ExamTimetable examTimetable,String examDate,String examEndTime) {
	    examTimetableMapper.insert(examTimetable);
    }
    
    //通过exam查询数据库中是否存在
    public List<ExamTimetable> selectByExam(Exam exam,String examDate,String examEndTime) {
		ExamTimetableExample example = new ExamTimetableExample();
		example.createCriteria().andCourseNameEqualTo(exam.getCourse().getName()).andDayEqualTo(exam.getExamDay())
				.andExamDateEqualTo(DateUtils.localDateToDate(examDate, "yyyy-MM-dd HH:mm"))
				.andNameEqualTo(exam.getExamName()).andRoomNameEqualTo(exam.getClassRoom().getName())
				.andSchoolWeekEqualTo(exam.getExamWeekOfTerm())
				.andStartTimeEqualTo(DateUtils.localDateToDate(examDate, "yyyy-MM-dd HH:mm"))
				.andEndTimeEqualTo(DateUtils.localDateToDate(examEndTime, "yyyy-MM-dd HH:mm"));
		return examTimetableMapper.selectByExample(example);
    }
}
