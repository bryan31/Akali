package com.yomahub.akali.manager;

import cn.hutool.core.util.StrUtil;
import com.alibaba.csp.sentinel.util.function.Tuple2;
import com.yomahub.akali.enums.AkaliStrategyEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Map;

public class AkaliMethodManager {

    private final static Logger log = LoggerFactory.getLogger(AkaliMethodManager.class);

    private final static Map<String, Tuple2<AkaliStrategyEnum, Annotation>> akaliMethodMap = new HashMap<>();

    public static void addMethodStr(String methodStr, Tuple2<AkaliStrategyEnum, Annotation> tuple){
        log.info(StrUtil.format("[AKALI] Register akali method:[{}][{}]", tuple.r1.name(), methodStr));
        akaliMethodMap.put(methodStr, tuple);
    }

    public static Tuple2<AkaliStrategyEnum, Annotation> getAnnoInfo(String methodStr){
        return akaliMethodMap.get(methodStr);
    }

    public static boolean contain(String methodStr){
        return akaliMethodMap.containsKey(methodStr);
    }
}
