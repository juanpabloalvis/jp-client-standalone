package com.jp.client.standalone.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.loadbalancer.core.ServiceInstanceListSupplier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

import java.util.List;

@Configuration
public class CustomLoadBalancerConfig {

    @Autowired
    private DiscoveryClient discoveryClient;
    String serviceId = "JP-CONFIG";

    @Autowired
    private WebClient.Builder loadBalancedWebClientBuilder;


    @Bean
    ServiceInstanceListSupplier serviceInstanceListSupplier() {

        return new ServiceInstanceListSupplier() {
            @Override
            public String getServiceId() {
                return serviceId;
            }

            @Override
            public Flux<List<ServiceInstance>> get() {
                List<ServiceInstance> instances = discoveryClient.getInstances(serviceId);

//        return Flux.just(Arrays
//                .asList(new DefaultServiceInstance(serviceId + "1", serviceId, "localhost", 8081, false),
//                        new DefaultServiceInstance(serviceId + "2", serviceId, "localhost", 8082, false)));
//
                return Flux.just(instances);
            }
        };
    }
}