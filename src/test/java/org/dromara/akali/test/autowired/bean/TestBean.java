package org.dromara.akali.test.autowired.bean;

import org.dromara.akali.annotation.AkaliFallback;
import org.dromara.akali.annotation.AkaliHot;
import org.dromara.akali.enums.FlowGradeEnum;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class TestBean {

    @Resource
    private TestDomain testDomain;

    @AkaliFallback(grade = FlowGradeEnum.FLOW_GRADE_QPS, count = 2)
    public String sayHi1(String name){
        return testDomain.process(name);
    }

    @AkaliHot(grade = FlowGradeEnum.FLOW_GRADE_QPS, count = 8, duration = 2)
    public String sayHi2(String name){
        try {
            Thread.sleep(200);
        }catch (Exception ignored){
        }
        return testDomain.process(name);
    }


    public String sayHi1Fallback(String name){
        return "fallback return";
    }
}
