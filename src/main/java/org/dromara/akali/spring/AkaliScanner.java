package org.dromara.akali.spring;

import cn.hutool.core.annotation.AnnotationUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.ReflectUtil;
import com.alibaba.csp.sentinel.util.MethodUtil;
import com.alibaba.csp.sentinel.util.function.Tuple2;
import org.dromara.akali.annotation.AkaliFallback;
import org.dromara.akali.annotation.AkaliHot;
import org.dromara.akali.enums.AkaliStrategyEnum;
import org.dromara.akali.manager.AkaliMethodManager;
import org.dromara.akali.manager.AkaliStrategyManager;
import org.dromara.akali.proxy.AkaliByteBuddyProxy;
import org.dromara.akali.strategy.AkaliStrategy;
import org.dromara.akali.util.ProxyUtil;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanInitializationException;
import org.springframework.beans.factory.config.InstantiationAwareBeanPostProcessor;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicBoolean;

public class AkaliScanner implements InstantiationAwareBeanPostProcessor {

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        Class<?> clazz = ProxyUtil.getUserClass(bean.getClass());

        if (AkaliStrategy.class.isAssignableFrom(clazz)){
            AkaliStrategyManager.addStrategy((AkaliStrategy) bean);
            return bean;
        }

        AtomicBoolean needProxy = new AtomicBoolean(false);
        Arrays.stream(clazz.getMethods()).forEach(method -> {
            AkaliFallback akaliFallback = searchAnnotation(method, AkaliFallback.class);
            if (ObjectUtil.isNotNull(akaliFallback)){
                AkaliMethodManager.addMethodStr(MethodUtil.resolveMethodName(method), new Tuple2<>(AkaliStrategyEnum.FALLBACK, akaliFallback));
                needProxy.set(true);
            }

            AkaliHot akaliHot = searchAnnotation(method, AkaliHot.class);
            if (ObjectUtil.isNotNull(akaliHot)){
                AkaliMethodManager.addMethodStr(MethodUtil.resolveMethodName(method), new Tuple2<>(AkaliStrategyEnum.HOT_METHOD, akaliHot));
                needProxy.set(true);
            }
        });

        if (needProxy.get()){
            try{
                AkaliByteBuddyProxy akaliProxy = new AkaliByteBuddyProxy(bean, clazz);
                return akaliProxy.proxy();
            }catch (Exception e){
                throw new BeanInitializationException(e.getMessage());
            }

        }else{
            return bean;
        }
    }

    private <A extends Annotation> A searchAnnotation(Method method, Class<A> annotationType){
        A anno = AnnotationUtil.getAnnotation(method, annotationType);
        //从接口层面向上搜索
        if (anno == null){
            Class<?>[] ifaces = method.getDeclaringClass().getInterfaces();

            for (Class<?> ifaceClass : ifaces){
                Method ifaceMethod = ReflectUtil.getMethod(ifaceClass, method.getName(), method.getParameterTypes());
                if (ifaceMethod != null) {
                    anno = searchAnnotation(ifaceMethod, annotationType);
                    break;
                }
            }
        }

        //从父类逐级向上搜索
        if (anno == null){
            Class<?> superClazz = method.getDeclaringClass().getSuperclass();
            if (superClazz != null){
                Method superMethod = ReflectUtil.getMethod(superClazz, method.getName(), method.getParameterTypes());
                if (superMethod != null){
                    return searchAnnotation(superMethod, annotationType);
                }else{
                    return null;
                }
            }else{
                return null;
            }
        }

        return anno;
    }
}
