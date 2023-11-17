package com.yomahub.akali.test.aopbean;

import com.yomahub.akali.test.aopbean.bean.TestBean;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.TestPropertySource;

import javax.annotation.Resource;

@TestPropertySource(value = "classpath:application.properties")
@SpringBootTest(classes = com.yomahub.akali.test.aopbean.AopBeanTest.class)
@EnableAutoConfiguration
@ComponentScan({"com.yomahub.akali.test.aopbean.aop", "com.yomahub.akali.test.aopbean.bean"})
public class AopBeanTest {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Resource
    private TestBean testBean;

    @Test
    public void test1(){
        for (int i = 0; i < 10; i++) {
            log.info(testBean.sayHi1("jack"));
        }
    }

    @Test
    public void test2(){
        for (int i = 0; i < 50; i++) {
            log.info(testBean.sayHi2("jack"));
        }
    }
}

