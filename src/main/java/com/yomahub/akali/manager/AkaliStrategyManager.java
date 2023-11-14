package com.yomahub.akali.manager;

import com.yomahub.akali.enums.AkaliStrategyEnum;
import com.yomahub.akali.strategy.AkaliStrategy;

import java.util.HashMap;
import java.util.Map;

public class AkaliStrategyManager {

    private final static Map<AkaliStrategyEnum, AkaliStrategy> strategyMap = new HashMap<>();

    public static void addStrategy(AkaliStrategy akaliStrategy){
        strategyMap.put(akaliStrategy.getStrategy(), akaliStrategy);
    }

    public static AkaliStrategy getStrategy(AkaliStrategyEnum strategyEnum){
        return strategyMap.get(strategyEnum);
    }
}
