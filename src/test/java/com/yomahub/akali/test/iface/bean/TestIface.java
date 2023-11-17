package com.yomahub.akali.test.iface.bean;

import com.yomahub.akali.annotation.AkaliFallback;
import com.yomahub.akali.annotation.AkaliHot;
import com.yomahub.akali.enums.FlowGradeEnum;

public interface TestIface {

    @AkaliFallback(grade = FlowGradeEnum.FLOW_GRADE_QPS, count = 2)
    String sayHi1(String name);

    @AkaliHot(grade = FlowGradeEnum.FLOW_GRADE_QPS, count = 8, duration = 2)
    String sayHi2(String name);


    String sayHi1Fallback(String name);
}
