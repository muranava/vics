package com.infinityworks.webapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jmx.JmxAutoConfiguration;
import org.springframework.boot.autoconfigure.websocket.WebSocketAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableAutoConfiguration(exclude = {
        WebSocketAutoConfiguration.class,
        JmxAutoConfiguration.class
})
@ComponentScan
public class Application {
    public static void main(String... args) {
        SpringApplication.run(Application.class, args);
    }
}
