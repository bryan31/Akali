package com.yomahub.akali.test.absClass.bean;

import com.yomahub.akali.annotation.AkaliFallback;
import com.yomahub.akali.annotation.AkaliHot;
import com.yomahub.akali.enums.FlowGradeEnum;
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
