package org.dromara.akali.test.autowired.bean;

import org.springframework.stereotype.Component;

@Component
public class TestDomain {

    public String process(String name){
        return "hi," + name;
    }
}
