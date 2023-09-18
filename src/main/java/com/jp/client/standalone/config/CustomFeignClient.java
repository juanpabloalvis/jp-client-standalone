package com.jp.client.standalone.config;

import org.springframework.cloud.loadbalancer.annotation.LoadBalancerClient;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "JP-CONFIG")
@LoadBalancerClient(name = "JP-CONFIG", configuration = CustomLoadBalancerConfig.class)
public interface CustomFeignClient {
    @GetMapping(value = "/application-name")
    ResponseEntity<String> getApp();
}
