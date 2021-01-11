package com.hackerda.platform.application;

import com.hackerda.platform.domain.course.timetable.CourseTimeTableOverview;
import com.hackerda.platform.domain.course.timetable.CourseTimetableBO;
import com.hackerda.platform.domain.course.timetable.CourseTimetableRepository;
import com.hackerda.platform.domain.course.timetable.CourseTimetableSpiderService;
import com.hackerda.platform.domain.student.StudentAccount;
import com.hackerda.platform.domain.student.StudentRepository;
import com.hackerda.platform.domain.student.StudentUserBO;
import com.hackerda.platform.domain.time.SchoolTime;
import com.hackerda.platform.domain.time.Term;
import org.apache.commons.collections.CollectionUtils;
import org.joda.time.field.PreciseDateTimeField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

@Service
public class CourseTimetableQueryApp {
    @Autowired
    private CourseTimetableRepository courseTimetableRepository;
    @Autowired
    private CourseTimetableSpiderService courseTimetableSpiderService;

    private final Map<String, ReentrantLock> lockMap = new ConcurrentHashMap<>();


    public CourseTimeTableOverview getByStudent(StudentUserBO studentUserBO, Term term){

        if(!studentUserBO.isMsgHasCheck()) {
            return getByClassId(studentUserBO.getUrpClassNum().toString(), term);
        }

        CourseTimeTableOverview byAccount = courseTimetableRepository.getByAccount(studentUserBO, term);

        if(!byAccount.isEmpty()) {
            return byAccount;
        }

        CourseTimeTableOverview fetch = courseTimetableSpiderService.fetchByStudent(studentUserBO);
        if (!fetch.isEmpty() && fetch.getTerm().equals(term)) {
            courseTimetableRepository.saveByStudent(fetch.getNewList(), studentUserBO);
            return fetch;
        }

        return getByClassId(studentUserBO.getUrpClassNum().toString(), term);
    }

    public CourseTimeTableOverview getByClassId(String classId, Term term) {

        ReentrantLock lock = getClassLock(classId, term);

        try {
            if (lock.tryLock(8000L, TimeUnit.MILLISECONDS)) {
                try {
                    CourseTimeTableOverview byClassId = courseTimetableRepository.getByClassId(classId, term);

                    if(!byClassId.isEmpty()) {
                        return byClassId;
                    }

                    CourseTimeTableOverview overview = courseTimetableSpiderService.fetchByClassId(classId, term);
                    courseTimetableRepository.saveByClass(overview.getNewList(), classId);
                    return overview;
                } finally {
                    lock.unlock();
                }

            } else {
                return CourseTimeTableOverview.fromRepo(Collections.emptyList(), false);
            }
        } catch (InterruptedException e) {
            return CourseTimeTableOverview.fromRepo(Collections.emptyList(), false);
        }

    }

    private synchronized ReentrantLock getClassLock(String classId, Term term) {
        ReentrantLock newLock = new ReentrantLock();
        ReentrantLock lock = lockMap.putIfAbsent(classId + term.asKey(), newLock);

        return lock != null ? lock : newLock;
    }
}
