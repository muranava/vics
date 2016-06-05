package com.infinityworks.pdfserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class PdfServerApp {
    @RequestMapping(method = RequestMethod.GET, value = "/ping")
    public String ping() {
        return "pong";
    }

    public static void main(String... args) {
        SpringApplication.run(PdfServerApp.class, args);
    }
}
