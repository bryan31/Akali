package com.yomahub.akali.proxy;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.csp.sentinel.SphO;
import com.alibaba.csp.sentinel.util.MethodUtil;
import com.yomahub.akali.annotation.AkaliFallback;
import com.yomahub.akali.annotation.AkaliHot;
import com.yomahub.akali.enums.AkaliStrategyEnum;
import com.yomahub.akali.manager.AkaliMethodManager;
import com.yomahub.akali.manager.AkaliRuleManager;
import com.yomahub.akali.manager.AkaliStrategyManager;
import com.yomahub.akali.sph.SphEngine;
import com.yomahub.akali.strategy.AkaliStrategy;
import net.bytebuddy.ByteBuddy;
import net.bytebuddy.implementation.InvocationHandlerAdapter;
import net.bytebuddy.matcher.ElementMatchers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.List;

public class AkaliProxy {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private final Object bean;

    private final List<Method> fallbackMethodList;

    private final List<Method> hotspotMethodList;

    public AkaliProxy(Object bean, List<Method> fallbackMethodList, List<Method> hotspotMethodList) {
        this.bean = bean;
        this.fallbackMethodList = fallbackMethodList;
        this.hotspotMethodList = hotspotMethodList;
    }

    public Object proxy() throws Exception{
        Collection<Method> methodList = CollUtil.union(fallbackMethodList, hotspotMethodList);

        return new ByteBuddy().subclass(bean.getClass())
                .name(StrUtil.format("{}$ByteBuddy${}", bean.getClass().getSimpleName(), IdUtil.fastSimpleUUID()))
                .method(ElementMatchers.namedOneOf(methodList.stream().map(Method::getName).toArray(String[]::new)))
                .intercept(InvocationHandlerAdapter.of(new AopInvocationHandler()))
                .annotateType(bean.getClass().getAnnotations())
                .make()
                .load(AkaliProxy.class.getClassLoader())
                .getLoaded()
                .newInstance();
    }

    public class AopInvocationHandler implements InvocationHandler {

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            String methodStr = MethodUtil.resolveMethodName(method);

            if (AkaliMethodManager.contain(methodStr)){
                AkaliStrategyEnum akaliStrategyEnum = AkaliMethodManager.getAnnoInfo(methodStr).r1;
                Annotation anno = AkaliMethodManager.getAnnoInfo(methodStr).r2;

                if (anno instanceof AkaliFallback){
                    AkaliRuleManager.registerFallbackRule((AkaliFallback) anno, method);
                }else if (anno instanceof AkaliHot){
                    AkaliRuleManager.registerHotRule((AkaliHot) anno, method);
                }else{
                    throw new RuntimeException("annotation type error");
                }

                return SphEngine.process(bean, method, args, methodStr, akaliStrategyEnum);
            }else {
                return method.invoke(bean, args);
            }
        }
    }
}
