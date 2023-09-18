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
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

@SpringBootApplication
@EnableFeignClients
@EnableDiscoveryClient
public class JpClientStandaloneApplication implements ApplicationRunner {

    private static final Logger log = LoggerFactory.getLogger(JpClientStandaloneApplication.class);
    @Autowired
    private EurekaClient eurekaClient;

    @Autowired
    private CustomFeignClient customFeignClient;

    @Autowired
    private DiscoveryClient discoveryClient;


    public static void main(String[] args) {
        SpringApplication.run(JpClientStandaloneApplication.class, args);
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        printApplicationInformation();
        printApplicationInformationV2();
        printApplicationInformationV3();
    }

    private void printApplicationInformationV3() {
        List<ServiceInstance> instances = discoveryClient.getInstances("JP-CONFIG");
        instances.forEach(serviceInstance -> {
                    try {
                        HttpRequest request = HttpRequest.newBuilder()
                                .uri(serviceInstance.getUri().resolve("/application-name"))
                                .GET()
                                .build();
                        HttpClient client = HttpClient.newHttpClient();

                        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
                        log.info(response.body());

                    } catch (IOException | InterruptedException e) {
                        log.error(e.getMessage());
                        Thread.currentThread().interrupt();
                    }

                }
        );
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
