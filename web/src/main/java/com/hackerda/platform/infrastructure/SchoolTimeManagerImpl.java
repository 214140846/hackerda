package com.hackerda.platform.infrastructure;

import com.hackerda.platform.domain.time.SchoolTime;
import com.hackerda.platform.domain.time.SchoolTimeManager;
import com.hackerda.platform.domain.time.Term;
import com.hackerda.platform.utils.DateUtils;
import org.springframework.stereotype.Service;

@Service
public class SchoolTimeManagerImpl implements SchoolTimeManager {

    @Override
    public Term getCourseTimeTableTerm() {
        return new Term(2020, 2021, 2);
    }

    @Override
    public SchoolTime getCurrentSchoolTime() {
        return DateUtils.getCurrentSchoolTime();
    }
}
