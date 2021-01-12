package com.hackerda.platform.domain.course.timetable;

import com.hackerda.platform.domain.time.Term;
import lombok.Data;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

@Data
public class CourseTimeTableOverview {

    private List<CourseTimetableBO> courseTimetableBOList;

    private List<Integer> termShowList;

    private List<Integer> termRestList;

    private boolean isCurrentTerm;

    private boolean isPersonal;

    private boolean fetchSuccess;

    private boolean fromRepository;

    private String errorMsg;

    private int errorCode;

    private CourseTimeTableOverview() {

    }

    private CourseTimeTableOverview (List<CourseTimetableBO> courseTimetableBOList) {
        this.courseTimetableBOList = courseTimetableBOList;
        courseTimetableBOList.sort(CourseTimetableBO::compareTo);

        int index = 0;
        for (CourseTimetableBO bo : courseTimetableBOList) {
            bo.setIndex(index ++);
        }


        PriorityQueue<CourseTimetableBO> priorityQueue =
                new PriorityQueue<>(Comparator.comparing(CourseTimetableBO::getOrder, Comparator.reverseOrder()));
        termRestList = new ArrayList<>();

        for (CourseTimetableBO bo : courseTimetableBOList) {
            if(priorityQueue.isEmpty() || priorityQueue.peek().getOrder() < bo.getOrder()) {
                priorityQueue.add(bo);
            } else {
                termRestList.add(bo.getIndex());
            }
        }

        termShowList = priorityQueue.stream().map(CourseTimetableBO::getIndex).sorted().collect(Collectors.toList());
    }

    public List<CourseTimetableBO> getNewList(){

        if(fromRepository || !fetchSuccess){
            return Collections.emptyList();
        }
        return courseTimetableBOList;
    }

    public boolean isEmpty(){
        return CollectionUtils.isEmpty(courseTimetableBOList);
    }

    public Term getTerm() {

        if (isEmpty()) {
            throw new UnsupportedOperationException("table is empty");
        }

        return courseTimetableBOList.get(0).getTerm();

    }


    public static CourseTimeTableOverview fromRepo(List<CourseTimetableBO> courseTimetableBOList, boolean isPersonal) {
        CourseTimeTableOverview overview = new CourseTimeTableOverview(courseTimetableBOList);
        overview.fromRepository = true;
        overview.errorCode = 0;
        overview.isPersonal = isPersonal;
        return overview;
    }

    public static CourseTimeTableOverview ofFetchSuccess(List<CourseTimetableBO> courseTimetableBOList,
                                                     boolean isPersonal) {
        CourseTimeTableOverview overview = new CourseTimeTableOverview(courseTimetableBOList);
        overview.fetchSuccess = true;
        overview.errorCode = 0;
        overview.isPersonal = isPersonal;
        return overview;
    }

    public static CourseTimeTableOverview ofFetchFail(int errorCode, String msg, boolean isPersonal) {
        CourseTimeTableOverview overview = new CourseTimeTableOverview();
        overview.errorCode = errorCode;
        overview.errorMsg = msg;
        overview.isPersonal = isPersonal;
        return overview;
    }

}
