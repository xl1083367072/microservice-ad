package com.xl.ad;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@EnableEurekaServer
@SpringBootApplication
public class EurekaApplication8000 {

        public static void main(String[] args) {
            SpringApplication.run(EurekaApplication8000.class,args);
        }

}
