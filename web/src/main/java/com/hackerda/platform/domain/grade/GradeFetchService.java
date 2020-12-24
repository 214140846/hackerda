package com.hackerda.platform.domain.grade;

import com.hackerda.platform.domain.student.StudentUserBO;

import java.util.List;

public interface GradeFetchService {

    List<TermGradeBO> getAllByStudent(StudentUserBO student);

}
