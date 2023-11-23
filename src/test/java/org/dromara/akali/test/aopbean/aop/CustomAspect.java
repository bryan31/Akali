package org.dromara.akali.test.aopbean.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
@Aspect
public class CustomAspect {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Pointcut("execution(* org.dromara.akali.test.aopbean.bean.*.*(..))")
    public void cut() {
    }

    @Around("cut()")
    public Object around(ProceedingJoinPoint jp) throws Throwable {
        log.info("process aspect");
        return jp.proceed();
    }
}
