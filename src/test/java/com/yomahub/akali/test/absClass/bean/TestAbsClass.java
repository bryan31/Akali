package com.yomahub.akali.test.absClass.bean;

import com.yomahub.akali.annotation.AkaliFallback;
import com.yomahub.akali.annotation.AkaliHot;
import com.yomahub.akali.enums.FlowGradeEnum;

public abstract class TestAbsClass {

    @AkaliFallback(grade = FlowGradeEnum.FLOW_GRADE_QPS, count = 2)
    public abstract String sayHi1(String name);

    @AkaliHot(grade = FlowGradeEnum.FLOW_GRADE_QPS, count = 8, duration = 2)
    public abstract String sayHi2(String name);


    public abstract String sayHi1Fallback(String name);
}
