package org.dromara.akali.proxy;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.csp.sentinel.SphO;
import com.alibaba.csp.sentinel.util.MethodUtil;
import org.dromara.akali.annotation.AkaliFallback;
import org.dromara.akali.annotation.AkaliHot;
import org.dromara.akali.enums.AkaliStrategyEnum;
import org.dromara.akali.manager.AkaliMethodManager;
import org.dromara.akali.manager.AkaliRuleManager;
import org.dromara.akali.manager.AkaliStrategyManager;
import org.dromara.akali.sph.SphEngine;
import org.dromara.akali.strategy.AkaliStrategy;
import org.dromara.akali.util.SerialsUtil;
import net.bytebuddy.ByteBuddy;
import net.bytebuddy.dynamic.loading.ClassLoadingStrategy;
import net.bytebuddy.dynamic.loading.ClassReloadingStrategy;
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
                .name(StrUtil.format("{}$ByteBuddy${}", bean.getClass().getName(), SerialsUtil.generateShortUUID()))
                .implement(bean.getClass().getInterfaces())
                .method(ElementMatchers.namedOneOf(methodList.stream().map(Method::getName).toArray(String[]::new)))
                .intercept(InvocationHandlerAdapter.of(new AopInvocationHandler()))
                .annotateType(bean.getClass().getAnnotations())
                .make()
                .load(AkaliProxy.class.getClassLoader(), ClassLoadingStrategy.Default.INJECTION)
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
