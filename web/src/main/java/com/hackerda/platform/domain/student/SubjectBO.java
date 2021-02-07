package com.hackerda.platform.domain.student;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@AllArgsConstructor
public class SubjectBO {

    private final String grade;

    private final String name;

    private final String num;
}
