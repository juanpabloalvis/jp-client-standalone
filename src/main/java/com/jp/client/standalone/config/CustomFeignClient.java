package com.jp.client.standalone.config;

import org.springframework.cloud.loadbalancer.annotation.LoadBalancerClient;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(name = "JP-CONFIG")
@LoadBalancerClient(name = "JP-CONFIG", configuration = CustomLoadBalancerConfig.class)
public interface CustomFeignClient {
    @RequestMapping(method = RequestMethod.GET, value = "/application-name")
    ResponseEntity<String> getApp();
}
