package com.hackerda.platform.domain.student;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.ToString;

/**
 * @author JR Chan
 */
@ToString
@AllArgsConstructor
@Getter
public class ClazzInfoBO {

    private final AcademyBO academyBO;

    private final SubjectBO subjectBO;

    private final ClazzBO clazzBO;


}
