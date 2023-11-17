package com.yomahub.akali.test.iface.bean;

import org.springframework.stereotype.Component;

@Component
public class TestBean implements TestIface {

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
