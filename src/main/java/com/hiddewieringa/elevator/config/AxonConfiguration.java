package com.hiddewieringa.elevator.config;

import org.axonframework.config.DefaultConfigurer;
import org.axonframework.queryhandling.DefaultQueryGateway;
import org.axonframework.queryhandling.QueryBus;
import org.axonframework.queryhandling.QueryGateway;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AxonConfiguration {
//
//    @Bean
//    QueryGateway queryGateway(QueryBus queryBus) {
//        return new DefaultQueryGateway(queryBus);
//    }
}
