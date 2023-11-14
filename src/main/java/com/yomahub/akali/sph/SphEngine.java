package com.yomahub.akali.sph;

import cn.hutool.crypto.digest.DigestUtil;
import com.alibaba.csp.sentinel.Entry;
import com.alibaba.csp.sentinel.EntryType;
import com.alibaba.csp.sentinel.SphO;
import com.alibaba.csp.sentinel.SphU;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.alibaba.fastjson.JSON;
import com.yomahub.akali.enums.AkaliStrategyEnum;
import com.yomahub.akali.manager.AkaliStrategyManager;

import java.lang.reflect.Method;

public class SphEngine {

    public static Object process(Object bean, Method method, Object[] args, String methodStr, AkaliStrategyEnum akaliStrategyEnum) throws Exception{
        switch (akaliStrategyEnum){
            case FALLBACK:
                if (SphO.entry(methodStr)){
                    try{
                        return method.invoke(bean, args);
                    }finally {
                        SphO.exit();
                    }
                }else{
                    return AkaliStrategyManager.getStrategy(akaliStrategyEnum).process(bean, method, args);
                }
            case HOT_METHOD:
                String convertParam = DigestUtil.md5Hex(JSON.toJSONString(args));
                Entry entry = null;
                try{
                    entry = SphU.entry(methodStr, EntryType.IN, 1, convertParam);
                    return method.invoke(bean, args);
                }catch (BlockException e){
                    return AkaliStrategyManager.getStrategy(akaliStrategyEnum).process(bean, method, args);
                }finally {
                    if (entry != null){
                        entry.exit(1, convertParam);
                    }
                }
            default:
                throw new Exception("[AKALI] Strategy error!");
        }
    }
}
