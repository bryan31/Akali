package com.yomahub.akali.spring;

import cn.hutool.core.annotation.AnnotationUtil;
import cn.hutool.core.util.ObjectUtil;
import com.alibaba.csp.sentinel.util.MethodUtil;
import com.alibaba.csp.sentinel.util.function.Tuple2;
import com.yomahub.akali.annotation.AkaliFallback;
import com.yomahub.akali.annotation.AkaliHot;
import com.yomahub.akali.enums.AkaliStrategyEnum;
import com.yomahub.akali.manager.AkaliMethodManager;
import com.yomahub.akali.manager.AkaliStrategyManager;
import com.yomahub.akali.proxy.AkaliProxy;
import com.yomahub.akali.strategy.AkaliStrategy;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanInitializationException;
import org.springframework.beans.factory.config.InstantiationAwareBeanPostProcessor;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class AkaliScanner implements InstantiationAwareBeanPostProcessor {

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        Class<?> clazz = bean.getClass();

        if (AkaliStrategy.class.isAssignableFrom(clazz)){
            AkaliStrategyManager.addStrategy((AkaliStrategy) bean);
            return bean;
        }

        AtomicBoolean needProxy = new AtomicBoolean(false);
        List<Method> fallbackMethodList = new ArrayList<>();
        List<Method> hotspotMethodList = new ArrayList<>();
        Arrays.stream(clazz.getDeclaredMethods()).forEach(method -> {
            AkaliFallback akaliFallback = AnnotationUtil.getAnnotation(method, AkaliFallback.class);
            if (ObjectUtil.isNotNull(akaliFallback)){
                fallbackMethodList.add(method);
                AkaliMethodManager.addMethodStr(MethodUtil.resolveMethodName(method), new Tuple2<>(AkaliStrategyEnum.FALLBACK, akaliFallback));
                needProxy.set(true);
            }

            AkaliHot akaliHot = AnnotationUtil.getAnnotation(method, AkaliHot.class);
            if (ObjectUtil.isNotNull(akaliHot)){
                hotspotMethodList.add(method);
                AkaliMethodManager.addMethodStr(MethodUtil.resolveMethodName(method), new Tuple2<>(AkaliStrategyEnum.HOT_METHOD, akaliHot));
                needProxy.set(true);
            }
        });

        if (needProxy.get()){
            try{
                AkaliProxy akaliProxy = new AkaliProxy(bean, fallbackMethodList, hotspotMethodList);
                return akaliProxy.proxy();
            }catch (Exception e){
                throw new BeanInitializationException(e.getMessage());
            }

        }else{
            return bean;
        }
    }
}
