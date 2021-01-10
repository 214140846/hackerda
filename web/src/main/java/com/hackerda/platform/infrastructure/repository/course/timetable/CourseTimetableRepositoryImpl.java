package com.hackerda.platform.infrastructure.repository.course.timetable;

import com.hackerda.platform.MDCThreadPool;
import com.hackerda.platform.domain.SpiderSwitch;
import com.hackerda.platform.domain.student.StudentUserBO;
import com.hackerda.platform.domain.student.WechatStudentUserBO;
import com.hackerda.platform.domain.time.Term;
import com.hackerda.platform.infrastructure.database.dao.ClassCourseTimetableDao;
import com.hackerda.platform.infrastructure.database.dao.CourseDao;
import com.hackerda.platform.infrastructure.database.dao.CourseTimeTableDao;
import com.hackerda.platform.domain.course.timetable.CourseTimeTableOverview;
import com.hackerda.platform.domain.course.timetable.CourseTimetableBO;
import com.hackerda.platform.domain.course.timetable.CourseTimetableRepository;
import com.hackerda.platform.infrastructure.database.model.*;
import com.hackerda.platform.infrastructure.repository.ExceptionMsg;
import com.hackerda.platform.infrastructure.repository.FetchExceptionHandler;
import com.hackerda.platform.infrastructure.repository.course.CourseAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.HashSet;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;


@Repository
public class CourseTimetableRepositoryImpl implements CourseTimetableRepository {

    @Autowired
    private CourseTimeTableDao courseTimeTableDao;
    @Autowired
    private CourseTimetableAdapter courseTimetableAdapter;
    @Autowired
    private CourseAdapter courseAdapter;
    @Autowired
    private ClassCourseTimetableDao classCourseTimetableDao;
    @Autowired
    private CourseDao courseDao;

    @Override
    public CourseTimeTableOverview getByAccount(StudentUserBO wechatStudentUserBO, Term term) {

        StudentCourseTimeTable courseTimeTable = new StudentCourseTimeTable()
                .setStudentId(wechatStudentUserBO.getAccount().getInt())
                .setTermYear(term.getTermYear())
                .setTermOrder(term.getOrder());

        List<CourseTimetableDetailDO> detailList = courseTimeTableDao.selectDetailByStudent(courseTimeTable);
        List<CourseTimetableBO> collect = detailList.stream().map(x -> courseTimetableAdapter.toBO(x)).collect(Collectors.toList());

        return CourseTimeTableOverview.fromRepo(collect, true);

    }


    @Override
    public CourseTimeTableOverview getByClassId(String classId, Term term) {

        ClassCourseTimetable relative = new ClassCourseTimetable()
                .setClassId(classId)
                .setTermYear(term.getTermYear())
                .setTermOrder(term.getOrder());

        List<CourseTimetableDetailDO> timetableList = courseTimeTableDao.selectDetailByClassId(relative);
        List<CourseTimetableBO> collect = timetableList.stream().map(x -> courseTimetableAdapter.toBO(x)).collect(Collectors.toList());

        return CourseTimeTableOverview.fromRepo(collect, false);

    }


    @Override
    @Transactional
    public void saveByStudent(List<CourseTimetableBO> tableList, StudentUserBO wechatStudentUserBO) {

        if(CollectionUtils.isEmpty(tableList)){
            return;
        }

        List<CourseTimetable> doList = tableList.stream().map(x -> courseTimetableAdapter.toDO(x)).collect(Collectors.toList());

        List<CourseTimetable> timetableList = courseTimeTableDao.selectBatchByKey(doList);
        List<Integer> idList = timetableList.stream().map(CourseTimetable::getId).collect(Collectors.toList());

        if (doList.size() != timetableList.size()) {
            HashSet<CourseTimetable> timetableHashSet = new HashSet<>(timetableList);
            List<CourseTimetable> rest = doList.stream().filter(x -> !timetableHashSet.contains(x)).collect(Collectors.toList());

            courseTimeTableDao.insertBatch(rest);

            idList.addAll(rest.stream().map(CourseTimetable::getId).collect(Collectors.toList()));
        }

        String termYear = tableList.get(0).getTermYear();
        Integer termOrder = tableList.get(0).getTermOrder();

        List<StudentCourseTimeTable> relativeList = idList.stream().map(x -> new StudentCourseTimeTable()
                .setCourseTimetableId(x)
                .setStudentId(wechatStudentUserBO.getAccount().getInt())
                .setTermYear(termYear)
                .setTermOrder(termOrder)).collect(Collectors.toList());

        courseDao.insertBatch(tableList.stream().map(x-> courseAdapter.toDO(x.getCourseBO())).collect(Collectors.toList()));
        courseTimeTableDao.insertBatchStudentRelative(relativeList);

    }

    @Override
    @Transactional
    public void saveByClass(List<CourseTimetableBO> tableList, String classId) {

        if(CollectionUtils.isEmpty(tableList)){
            return;
        }
        List<CourseTimetable> doList = tableList.stream().map(x -> courseTimetableAdapter.toDO(x)).collect(Collectors.toList());
        List<CourseTimetable> existInDB = courseTimeTableDao.selectBatchByKey(doList);

        List<ClassCourseTimetable> relativeList = existInDB.stream()
                .map(x -> x.getClassRelative(classId))
                .collect(Collectors.toList());

        if (tableList.size() != existInDB.size()){
            HashSet<CourseTimetable> existInDBSet = new HashSet<>(existInDB);

            List<CourseTimetable> rest = doList.stream()
                    .filter(x -> !existInDBSet.contains(x))
                    .collect(Collectors.toList());

            courseTimeTableDao.insertBatch(rest);

            relativeList.addAll(rest.stream().map(x-> x.getClassRelative(classId)).collect(Collectors.toList()));

        }

        courseDao.insertBatch(tableList.stream().map(x-> courseAdapter.toDO(x.getCourseBO())).distinct().collect(Collectors.toList()));
        classCourseTimetableDao.insertBatch(relativeList);

    }
}
