package com.xl.ad;

import com.sun.jersey.core.impl.provider.entity.XMLRootObjectProvider;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(XMLRootObjectProvider.App.class,args);
    }

}
