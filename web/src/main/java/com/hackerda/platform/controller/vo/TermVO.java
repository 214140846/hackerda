package com.hackerda.platform.controller.vo;

import lombok.Data;

@Data
public class TermVO {

    private int startYear;
    private int endYear;
    private int order;


    public TermVO() {

    }

    public TermVO (int startYear, int endYear, int order) {
        this.startYear = startYear;
        this.endYear = endYear;
        this.order = order;
    }
}
