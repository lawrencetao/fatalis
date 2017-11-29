package com.lawrence.fatalis.config.cxf;

import org.apache.cxf.bus.spring.BusDefinitionParser;
import org.apache.cxf.bus.spring.SpringBus;
import org.apache.cxf.feature.FastInfosetFeature;
import org.apache.cxf.feature.Feature;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Configuration
public class CxfConfig {

    /**
     * 配置springBus
     *
     * @return SpringBus
     */
    @Bean
    public SpringBus cxf() {
        SpringBus bus = new SpringBus();
        BusDefinitionParser.BusConfig busConfig = new BusDefinitionParser.BusConfig("clientBus");
        FastInfosetFeature feature = new FastInfosetFeature();
        feature.setForce(false);
        List<Feature> features = new ArrayList<>(Arrays.asList(feature));
        busConfig.setFeatures(features);
        bus.setBusConfig(busConfig);

        return bus;
    }

}