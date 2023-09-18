package com.jp.client.standalone;

import com.jp.client.standalone.config.CustomFeignClient;
import com.netflix.discovery.EurekaClient;
import com.netflix.discovery.shared.Application;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.http.ResponseEntity;

@SpringBootApplication
@EnableFeignClients
@EnableDiscoveryClient
public class JpClientStandaloneApplication implements ApplicationRunner {

    private static final Logger log = LoggerFactory.getLogger(JpClientStandaloneApplication.class);
    @Autowired
    private EurekaClient eurekaClient;

    @Autowired
    private CustomFeignClient customFeignClient;


    public static void main(String[] args) {
        SpringApplication.run(JpClientStandaloneApplication.class, args);
    }

    @Override
    public void run(ApplicationArguments args) {
        printApplicationInformation();
        printApplicationInformationV2();
    }

    private void printApplicationInformationV2() {
        for (int i = 0; i < 10; i++) {
            try {
                ResponseEntity<String> applicationName = customFeignClient.getApp();
                log.info("Status: {}", applicationName.getStatusCode());
                String body = applicationName.getBody();
                log.info("Body: {}", body);
            } catch (Exception e) {
                log.error(e.getMessage());
            }
        }

    }

    private void printApplicationInformation() {

//      lógica de negocio
//      aquí le voy a decir que traiga la info de esta app
        Application application = eurekaClient.getApplication("jp-config");
        log.info("Application Name [{}]", application.getName());
        application.getInstances().forEach(instanceInfo -> log.info("Ip Address: [{}:{}]", instanceInfo.getIPAddr(), instanceInfo.getPort()));

    }

}
