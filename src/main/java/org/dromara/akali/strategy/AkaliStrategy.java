package org.dromara.akali.strategy;

import org.dromara.akali.enums.AkaliStrategyEnum;
import org.springframework.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

public interface AkaliStrategy {

    AkaliStrategyEnum getStrategy();

    Object process(Object bean, Method method, Object[] args) throws Exception;
}
