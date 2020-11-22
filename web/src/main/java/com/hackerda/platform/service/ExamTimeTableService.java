package com.hackerda.platform.service;

import com.hackerda.platform.domain.student.StudentAccount;
import com.hackerda.platform.domain.student.WechatStudentUserBO;
import com.hackerda.platform.domain.student.StudentRepository;
import com.hackerda.platform.infrastructure.database.dao.ExamTimetableDao;
import com.hackerda.platform.infrastructure.database.dao.StudentExamTimeTableDao;
import com.hackerda.platform.infrastructure.database.model.*;
import com.hackerda.platform.infrastructure.database.model.example.ExamTimetable;
import com.hackerda.platform.utils.DateUtils;
import com.hackerda.platform.utils.Term;
import com.hackerda.spider.exception.PasswordUnCorrectException;
import com.hackerda.spider.exception.UrpRequestException;
import com.hackerda.spider.support.UrpExamTime;
import lombok.extern.slf4j.Slf4j;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * @author junrong.chen
 * @date 2018/11/28
 */
@Slf4j
@Service
public class ExamTimeTableService {
    @Resource
    private NewUrpSpiderService newUrpSpiderService;

    @Resource
    private RoomService roomService;

    @Resource
    private StudentRepository studentRepository;

    @Autowired
    private ExamTimetableDao examTimetableDao;
    
    @Autowired
    private StudentExamTimeTableDao studentExamTimeTableDao;

    public List<Exam> getExamTimeListFromSpider(int account) {

        WechatStudentUserBO student = studentRepository.findWetChatUser(new StudentAccount(account));
        if (student == null) {
            throw new PasswordUnCorrectException();
        }

        if (!student.isMsgHasCheck()) {
            return Collections.emptyList();
        }

        List<UrpExamTime> examTime;
        try {
            examTime = newUrpSpiderService.getExamTime(student);
        } catch (UrpRequestException e) {
           
            if (e.getCode() >= 500) {
            	List<ExamTimetable> res=examTimetableDao.getExamByAccount(String.valueOf(account));
            	return transform(res);
            }
            throw e;
        }
        List<Exam> examList=examTime.stream()
                .filter(x -> StringUtils.isNotEmpty(x.getDate()))
                .map(x -> {
                            if (StringUtils.isEmpty(x.getExamTime())) {
                                return new Exam()
                                        .setCourse(getCourseFromExamText(x.getCourseName()))
                                        .setDate(new Date())
                                        .setExamName(x.getExamName())
                                        .setClassRoom(new UrpClassroom());

                            }

                            String[] timeSplit = x.getExamTime().split("-");
                            return new Exam()
                                    .setCourse(getCourseFromExamText(x.getCourseName()))
                                    .setDate(DateUtils.localDateToDate(x.getDate(), DateUtils.DEFAULT_PATTERN))
                                    .setExamName(x.getExamName())
                                    .setStartTime(timeSplit[0])
                                    .setEndTime(timeSplit[1])
                                    .setClassRoom(getClassRoomFromText(x.getLocation()))
                                    .setExamDay(x.getWeek())
                                    .setExamWeekOfTerm(x.getWeekOfTerm());
                        }

                )
                .collect(Collectors.toList());
        List <ExamTimetable> examTimetableList=new ArrayList<>();
        //由于需要examTimetable表的id，所以需要主键返回，插入studentexamTimetable
        //需要有examTimetable的参与，创建列表存储需要被加入到studentexamTimetable表的examTimetable对象
        List <ExamTimetable> temp=new ArrayList<>();
        for (Exam exam : examList) {
			insertIfAbsent(exam, String.valueOf(account), examTimetableList, temp);
		}
		if (!CollectionUtils.isEmpty(examTimetableList)) {
			examTimetableDao.batchInsert(examTimetableList);
		}
		if (!CollectionUtils.isEmpty(temp)) {
			List<StudentExamTimetable> studentExamTimetableList = transform(temp, String.valueOf(account));
			studentExamTimeTableDao.insertBatch(studentExamTimetableList);
		}
		return examList;
    }


    /**
     * 由于这个接口非常耗时，而且请求量非常高，所以不再每次都从抓取结果，每天更新一次
     *
     * @param account
     * @return
     */
    public List<Exam> getExamTimeList(int account) {
        return getExamTimeListFromSpider(account);
    }

    private Course getCourseFromExamText(String examText) {

        String pattern = "（(.*?)-(.*?)）(.*)";
        // 创建 Pattern 对象
        Pattern r = Pattern.compile(pattern);

        // 现在创建 matcher 对象
        Matcher m = r.matcher(examText);

        if (m.find()) {
            String num = m.group(1);
            String order = m.group(2);
            String courseName = m.group(3);
            Course course = new Course().setCourseOrder(order).setName(courseName).setNum(num);
            if (course == null) {
                log.error("can not find course {} {}", num, order);
            }

            return course;

        } else {
            log.error("can not parse exam text {}", examText);
            return new Course();
        }

    }

    private UrpClassroom getClassRoomFromText(String date) {
        String[] split = date.split(" ");
        try {
            return roomService.getClassRoomByName(split[3]);
        }catch (Exception e) {
            UrpClassroom urpClassroom = new UrpClassroom();
            urpClassroom.setName(split[3]);
            urpClassroom.setTeachingBuildingName(split[2]);
            return urpClassroom;
        }

    }

	public void insertIfAbsent(Exam exam, String account, List<ExamTimetable> examTimetableList,
			List<ExamTimetable> temp) {
		if (exam == null) {
			return;
		}
		String baseDate = DateUtils.getDateStr(exam.getDate(), "yyyy-MM-dd ");
		String examDate = baseDate + exam.getStartTime();
		String examEndTime = baseDate + exam.getEndTime();
		ExamTimetable examTimetable = new ExamTimetable();
		Term term=DateUtils.getCurrentSchoolTime().getTerm();
		examTimetable.setCourseName(exam.getCourse().getName()).setDay(exam.getExamDay())
		  .setExamDate(DateUtils.localDateToDate(examDate, "yyyy-MM-dd HH:mm")).setGmtCreate(new Date())
		  .setGmtModify(new Date()).setName(exam.getExamName()).setRoomName(exam.getClassRoom().getName())
		  .setSchoolWeek(exam.getExamWeekOfTerm())
		  .setStartTime(DateUtils.localDateToDate(examDate, "yyyy-MM-dd HH:mm"))
		  .setEndTime(DateUtils.localDateToDate(examEndTime, "yyyy-MM-dd HH:mm"))
		  .setTermOrder(String.valueOf(term.getOrder())).setTermYear(term.getTermYear());
		ExamTimetable examIsExist = examTimetableDao.selectByExam(examTimetable);
		if (examIsExist == null) {
			examTimetableList.add(examTimetable);
			temp.add(examTimetable);
		} else {
			StudentExamTimetable stuExamIsExist = studentExamTimeTableDao.selectByAccountAndExamTimetable(account,
					examIsExist);
			if (stuExamIsExist == null) {
				temp.add(examIsExist);
			}
		}
	}
/**
   * 传入需要加入studentExamTimetable表中的列表由
 * ExamTimetable对象转化到StudentExamTimetable对象
 */
	public List<StudentExamTimetable> transform(List<ExamTimetable> temp, String account) {
		List<StudentExamTimetable> studentExamTimetableList = new ArrayList<>();
		Term term=DateUtils.getCurrentSchoolTime().getTerm();
		for (ExamTimetable examTimetable : temp) {
			StudentExamTimetable stuExam = new StudentExamTimetable();
			stuExam.setAccount(account).setExamTimetableId(examTimetable.getId())
			.setTermOrder(term.getOrder()).setTermYear(term.getTermYear())
			.setGmtCreate(new Date()).setGmtModify(new Date());
			studentExamTimetableList.add(stuExam);
		}
		return studentExamTimetableList;
	}
	
	public List<Exam> transform(List<ExamTimetable> temp){
		List<Exam> exam=new ArrayList<Exam>();
		for (ExamTimetable e : temp) {
			Exam etemp=new Exam();
			Course course=new Course();
			course.setName(e.getCourseName());
			UrpClassroom urproom=new UrpClassroom();
			urproom.setName(e.getRoomName());
			etemp.setStartTime(DateUtils.getDateStr(e.getStartTime(),"yyyy-MM-dd HH:mm"))
			.setEndTime(DateUtils.getDateStr(e.getEndTime(),"yyyy-MM-dd HH:mm"))
			.setDate(e.getStartTime()).setExamName(e.getName()).setExamWeekOfTerm(e.getSchoolWeek())
			.setExamDay(e.getDay()).setCourse(course).setClassRoom(urproom);	
			exam.add(etemp);
		}
		return exam;
	}
}
