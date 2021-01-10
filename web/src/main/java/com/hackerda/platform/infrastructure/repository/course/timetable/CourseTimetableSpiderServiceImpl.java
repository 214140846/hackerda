package com.hackerda.platform.infrastructure.repository.course.timetable;

import com.hackerda.platform.MDCThreadPool;
import com.hackerda.platform.domain.course.timetable.CourseTimeTableOverview;
import com.hackerda.platform.domain.course.timetable.CourseTimetableBO;
import com.hackerda.platform.domain.course.timetable.CourseTimetableSpiderService;
import com.hackerda.platform.domain.student.StudentUserBO;
import com.hackerda.platform.domain.time.Term;
import com.hackerda.platform.infrastructure.database.model.CourseTimetableDetailDO;
import com.hackerda.platform.infrastructure.repository.ExceptionMsg;
import com.hackerda.platform.infrastructure.repository.FetchExceptionHandler;
import com.hackerda.platform.infrastructure.repository.course.CourseAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
public class CourseTimetableSpiderServiceImpl implements CourseTimetableSpiderService {

    @Autowired
    private CourseTimetableSpiderFacade courseTimetableSpiderFacade;
    @Autowired
    private FetchExceptionHandler fetchExceptionHandler;
    @Autowired
    private CourseTimetableAdapter courseTimetableAdapter;

    private final Executor courseSpiderExecutor = new MDCThreadPool(7, 7, 0L, TimeUnit.SECONDS,
            new LinkedBlockingQueue<>(), r -> new Thread(r, "courseSpider"));

    @Override
    public CourseTimeTableOverview fetchByClassId(String classId, Term term) {
        CompletableFuture<List<CourseTimetableBO>> future =
                CompletableFuture.supplyAsync(() -> courseTimetableSpiderFacade.getByClassID(term.getTermYear(), term.getOrder(),
                        classId)
                        .stream().map(x -> courseTimetableAdapter.toBO(x)).collect(Collectors.toList()), courseSpiderExecutor);

        return getCourseTimeTableOverview(future);
    }

    @Override
    public CourseTimeTableOverview fetchByStudent(StudentUserBO student) {
        CompletableFuture<List<CourseTimetableBO>> future =
                CompletableFuture.supplyAsync(() -> courseTimetableSpiderFacade.getCurrentTermTableByAccount(student)
                        .stream().map(x -> courseTimetableAdapter.toBO(x)).collect(Collectors.toList()), courseSpiderExecutor);

        return getCourseTimeTableOverview(future);
    }

    private CourseTimeTableOverview getCourseTimeTableOverview(CompletableFuture<List<CourseTimetableBO>> future) {

        CourseTimeTableOverview overview = new CourseTimeTableOverview();

        try {
            List<CourseTimetableBO> tableForSpider = future.get(6000L, TimeUnit.MILLISECONDS);

            overview.setCourseTimetableBOList(tableForSpider);
            overview.setFetchSuccess(true);
            return overview;

        } catch (Throwable e) {
            ExceptionMsg handle = fetchExceptionHandler.handle(e);
            overview.setErrorCode(handle.getErrorCode());
            overview.setErrorMsg(handle.getMsg());
            overview.setFetchSuccess(false);

            return overview;
        }
    }
}
