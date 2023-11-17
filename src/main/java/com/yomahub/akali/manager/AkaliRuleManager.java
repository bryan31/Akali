package com.yomahub.akali.manager;

import cn.hutool.core.collection.ListUtil;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRule;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRuleManager;
import com.alibaba.csp.sentinel.slots.block.flow.param.ParamFlowRule;
import com.alibaba.csp.sentinel.slots.block.flow.param.ParamFlowRuleManager;
import com.alibaba.csp.sentinel.util.MethodUtil;
import com.yomahub.akali.annotation.AkaliFallback;
import com.yomahub.akali.annotation.AkaliHot;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;

public class AkaliRuleManager {
    private static final Logger log = LoggerFactory.getLogger(AkaliRuleManager.class);

    public static void registerFallbackRule(AkaliFallback akaliFallback, Method method){
        String resourceKey = MethodUtil.resolveMethodName(method);

        if (!FlowRuleManager.hasConfig(resourceKey)){
            FlowRule rule = new FlowRule();

            rule.setResource(resourceKey);
            rule.setGrade(akaliFallback.grade().getGrade());
            rule.setCount(akaliFallback.count());
            rule.setLimitApp("default");

            FlowRuleManager.loadRules(ListUtil.toList(rule));
            log.info("[AKALI] Add Fallback Rule [{}]", resourceKey);
        }
    }

    public static void registerHotRule(AkaliHot akaliHot, Method method){
        String resourceKey = MethodUtil.resolveMethodName(method);

        if (!ParamFlowRuleManager.hasRules(resourceKey)){
            ParamFlowRule rule = new ParamFlowRule();

            rule.setResource(MethodUtil.resolveMethodName(method));
            rule.setGrade(akaliHot.grade().getGrade());
            rule.setCount(akaliHot.count());
            rule.setDurationInSec(akaliHot.duration());
            rule.setParamIdx(0);

            ParamFlowRuleManager.loadRules(ListUtil.toList(rule));
            log.info("[AKALI] Add Hot Rule [{}]", rule.getResource());
        }
    }
}
