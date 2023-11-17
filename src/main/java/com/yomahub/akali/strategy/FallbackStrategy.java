package com.yomahub.akali.strategy;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;
import com.yomahub.akali.enums.AkaliStrategyEnum;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class FallbackStrategy implements AkaliStrategy{

    private final Map<String, Method> fallBackMethodMap = new ConcurrentHashMap<>();

    @Override
    public AkaliStrategyEnum getStrategy() {
        return AkaliStrategyEnum.FALLBACK;
    }

    @Override
    public Object process(Object bean, Method method, Object[] args) throws Exception{
        String fallbackMethodName = StrUtil.format("{}Fallback", method.getName());

        Method fallbackMethod;
        if (fallBackMethodMap.containsKey(fallbackMethodName)){
            fallbackMethod = fallBackMethodMap.get(fallbackMethodName);
        }else{
            fallbackMethod = ReflectUtil.getMethod(bean.getClass(), fallbackMethodName, method.getParameterTypes());
            fallBackMethodMap.put(fallbackMethodName, fallbackMethod);
        }

        if (ObjectUtil.isNull(fallbackMethod)){
            throw new RuntimeException(StrUtil.format("[AKALI] Can't find fallback method [{}] in bean [{}]", fallbackMethodName, bean.getClass().getName()));
        }

        return fallbackMethod.invoke(bean, args);
    }
}
