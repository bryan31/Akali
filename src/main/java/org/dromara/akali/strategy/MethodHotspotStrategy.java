package org.dromara.akali.strategy;

import cn.hutool.cache.CacheUtil;
import cn.hutool.cache.impl.TimedCache;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.digest.DigestUtil;
import com.alibaba.csp.sentinel.util.MethodUtil;
import com.alibaba.fastjson.JSON;
import org.dromara.akali.enums.AkaliStrategyEnum;
import org.dromara.akali.util.SegmentLock;

import java.lang.reflect.Method;

public class MethodHotspotStrategy implements AkaliStrategy {

    private TimedCache<String, Object> timedCache;
    private SegmentLock segmentLock;

    public MethodHotspotStrategy() {
        timedCache = CacheUtil.newTimedCache(1000 * 60);
        timedCache.schedulePrune(1000);
        segmentLock = new SegmentLock(64);
    }

    @Override
    public AkaliStrategyEnum getStrategy() {
        return AkaliStrategyEnum.HOT_METHOD;
    }

    @Override
    public Object process(Object bean, Method method, Object[] args) throws Exception {
        String hotKey = StrUtil.format("{}-{}", MethodUtil.resolveMethodName(method), DigestUtil.md5Hex(JSON.toJSONString(args)));


        if (timedCache.containsKey(hotKey)) {
            return timedCache.get(hotKey);
        } else {
            try {
                segmentLock.lockInterruptibleSafe(hotKey);
                if (timedCache.containsKey(hotKey)) {
                    return timedCache.get(hotKey);
                }
                Object result = method.invoke(bean, args);
                timedCache.put(hotKey, result);
                return result;
            }finally {
                segmentLock.unlock(hotKey);
            }
        }
    }
}
