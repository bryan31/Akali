package com.yomahub.akali.enums;

public enum FlowGradeEnum {

    FLOW_GRADE_THREAD(0),
    FLOW_GRADE_QPS(1);

    private int grade;

    FlowGradeEnum(int grade) {
        this.grade = grade;
    }

    public int getGrade() {
        return grade;
    }

    public void setGrade(int grade) {
        this.grade = grade;
    }
}
