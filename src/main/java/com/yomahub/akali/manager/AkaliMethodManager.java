package com.yomahub.akali.manager;

import com.alibaba.csp.sentinel.util.function.Tuple2;
import com.yomahub.akali.enums.AkaliStrategyEnum;

import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Map;

public class AkaliMethodManager {

    private final static Map<String, Tuple2<AkaliStrategyEnum, Annotation>> akaliMethodMap = new HashMap<>();

    public static void addMethodStr(String methodStr, Tuple2<AkaliStrategyEnum, Annotation> tuple){
        akaliMethodMap.put(methodStr, tuple);
    }

    public static Tuple2<AkaliStrategyEnum, Annotation> getAnnoInfo(String methodStr){
        return akaliMethodMap.get(methodStr);
    }

    public static boolean contain(String methodStr){
        return akaliMethodMap.containsKey(methodStr);
    }
}
