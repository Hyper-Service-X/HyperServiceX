package com.hsx.sa;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("com.hsx.*")
public class ServiceAdapterApplication {

    public static void main(String[] args) {
        SpringApplication.run(ServiceAdapterApplication.class, args);
    }
}
