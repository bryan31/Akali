package org.dromara.akali.spring;

import org.dromara.akali.strategy.FallbackStrategy;
import org.dromara.akali.strategy.MethodHotspotStrategy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AkaliAutoConfiguration {

    @Bean
    public AkaliScanner akaliScanner(){
        return new AkaliScanner();
    }

    @Bean
    public FallbackStrategy fallbackStrategy(){
        return new FallbackStrategy();
    }

    @Bean
    public MethodHotspotStrategy methodHotspotStrategy(){
        return new MethodHotspotStrategy();
    }
}
