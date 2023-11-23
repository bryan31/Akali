package org.dromara.akali.test.absClass.bean;

import org.dromara.akali.annotation.AkaliFallback;
import org.dromara.akali.annotation.AkaliHot;
import org.dromara.akali.enums.FlowGradeEnum;
import org.springframework.stereotype.Component;

@Component
public class TestBean extends TestAbsClass {

    public String sayHi1(String name){
        return "hi,"+name;
    }

    public String sayHi2(String name){
        try {
            Thread.sleep(200);
        }catch (Exception ignored){
        }
        return "hi,"+name;
    }


    public String sayHi1Fallback(String name){
        return "fallback return";
    }
}
