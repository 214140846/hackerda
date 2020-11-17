package com.hackerda.platform.infrastructure.database.dao;

import com.hackerda.platform.infrastructure.database.mapper.ExamTimetableMapper;
import com.hackerda.platform.infrastructure.database.model.Exam;
import com.hackerda.platform.infrastructure.database.model.StudentExamTimetable;
import com.hackerda.platform.infrastructure.database.model.example.ExamTimetable;
import com.hackerda.platform.infrastructure.database.model.example.ExamTimetableExample;
import com.hackerda.platform.utils.Term;
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

@Service
public class ExamTimetableDao {
    @Resource
    private ExamTimetableMapper examTimetableMapper;
    
    @Autowired
    private StudentExamTimeTableDao studentExamTimeTableDao;
    
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    
    private Term term=DateUtils.getCurrentSchoolTime().getTerm();
 
    public void insertIfAbsent(Exam exam,String account) {
		try {
			  if (exam == null) {
				return;
			  }
			String examDate = sdf.format(exam.getDate()).substring(0, 11)+exam.getStartTime();
			String examEndTime = sdf.format(exam.getDate()).substring(0, 11)+exam.getEndTime();
			List<ExamTimetable> list =selectByExam(exam, examDate,examEndTime);
			if (list!=null && list.size() == 0) {
				ExamTimetable e=insertByExam(exam,examDate,examEndTime);
				studentExamTimeTableDao.insert(account, e);			
			}else {
				List<StudentExamTimetable> seList = studentExamTimeTableDao.selectByAccountAndExamTimetable(account, list.get(0));
				if(seList!=null && seList.size()==0) {
				    studentExamTimeTableDao.insert(account, list.get(0));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

    }
    //通过查询出来的exam对象插入数据
    public ExamTimetable insertByExam(Exam exam,String examDate,String examEndTime) {
    	ExamTimetable examTimetable = new ExamTimetable();
		try {
			 examTimetable.setCourseName(exam.getCourse().getName()).setDay(exam.getExamDay())
					.setExamDate(sdf.parse(examDate)).setGmtCreate(new Date()).setGmtModify(new Date())
					.setName(exam.getExamName()).setRoomName(exam.getClassRoom().getName())
					.setSchoolWeek(exam.getExamWeekOfTerm()).setStartTime(sdf.parse(examDate)).
					setEndTime(sdf.parse(examEndTime))
					.setTermOrder(String.valueOf(term.getOrder()))
					.setTermYear(term.getTermYear());
			        examTimetableMapper.insert(examTimetable);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return examTimetable;
    }
    
    //通过exam查询数据库中是否存在
    public List<ExamTimetable> selectByExam(Exam exam,String examDate,String examEndTime) {
    	ExamTimetableExample example = new ExamTimetableExample();
		try {	
			  example.createCriteria().andCourseNameEqualTo(exam.getCourse().getName()).andDayEqualTo(exam.getExamDay())
					.andExamDateEqualTo(sdf.parse(examDate)).andNameEqualTo(exam.getExamName())
					.andRoomNameEqualTo(exam.getClassRoom().getName()).andSchoolWeekEqualTo(exam.getExamWeekOfTerm())
					.andStartTimeEqualTo(sdf.parse(examDate)).andEndTimeEqualTo(sdf.parse(examEndTime));	
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return examTimetableMapper.selectByExample(example);
    }
}
