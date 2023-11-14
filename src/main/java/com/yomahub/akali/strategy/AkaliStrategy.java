package com.yomahub.akali.strategy;

import com.yomahub.akali.enums.AkaliStrategyEnum;

import java.lang.reflect.Method;

public interface AkaliStrategy {

    AkaliStrategyEnum getStrategy();

    Object process(Object bean, Method method, Object[] args) throws Exception;
}
