package org.dromara.akali.manager;

import org.dromara.akali.enums.AkaliStrategyEnum;
import org.dromara.akali.strategy.AkaliStrategy;

import java.util.HashMap;
import java.util.Map;

public class AkaliStrategyManager {

    private static final Map<AkaliStrategyEnum, AkaliStrategy> strategyMap = new HashMap<>();

    public static void addStrategy(AkaliStrategy akaliStrategy){
        strategyMap.put(akaliStrategy.getStrategy(), akaliStrategy);
    }

    public static AkaliStrategy getStrategy(AkaliStrategyEnum strategyEnum){
        return strategyMap.get(strategyEnum);
    }
}
