package com.yomahub.akali.spring;

import com.yomahub.akali.strategy.FallbackStrategy;
import com.yomahub.akali.strategy.MethodHotspotStrategy;
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
