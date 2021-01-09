package com.hackerda.platform.domain.time;

import lombok.Data;

/**
 * @author JR Chan
 * @date 2019/5/1
 */
@Data
public class Term {
    /**
     * 如果是2018到2019年第一学期
     * startYear = 2018
     * endYear = 2019
     * order = 1
     */
    private int startYear;
    private int endYear;
    private int order;

    public Term(String term) {
        this.startYear = Integer.parseInt(term.substring(0, 4));
        this.endYear = Integer.parseInt(term.substring(5, 9));
        this.order = Integer.parseInt(term.substring(10, 11));
    }

    public Term(String termYear, int order) {
        this.startYear = Integer.parseInt(termYear.substring(0, 4));
        this.endYear = Integer.parseInt(termYear.substring(5, 9));
        this.order = order;
    }

    public Term(int startYear, int endYear, int order) {
        this.startYear = startYear;
        this.endYear = endYear;
        this.order = order;
    }

    public String getTermCode(){
        return this.getStartYear() + "-" + this.getEndYear();
    }

    public String getTermName(){
        if(this.order == 1){
            return "第一学期";
        } else if (this.order == 2){
            return "第二学期";
        } else {
            return "未知学期";
        }
    }


    public Term getNextTerm() {
        if (order == 1) {
            return new Term(this.startYear, this.endYear, this.order+ 1);
        } else {
            return new Term(this.startYear+1, this.endYear+1, 1);
        }
    }

    public String getTermYear(){
        return this.startYear + "-" + this.endYear;
    }

    public String getExecutiveEducationPlanNum() {
        return this.startYear + "-" + this.endYear + "-" + this.order+ "-1";
    }

    public String asKey() {
        return String.valueOf(startYear) + '-' + endYear + "-" + order;
    }

}
