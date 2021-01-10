package com.hackerda.platform.domain.course.timetable;

import com.hackerda.platform.domain.time.Term;
import lombok.Data;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;

@Data
public class CourseTimeTableOverview {

    private List<CourseTimetableBO> courseTimetableBOList;

    private boolean isCurrentTerm;

    private boolean isPersonal;

    private boolean fetchSuccess;

    private boolean fromRepository;

    private String errorMsg;

    private int errorCode;

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
        CourseTimeTableOverview overview = new CourseTimeTableOverview();
        overview.setCourseTimetableBOList(courseTimetableBOList);
        overview.fromRepository = true;
        overview.errorCode = 0;
        overview.isPersonal = isPersonal;
        return overview;
    }
}
