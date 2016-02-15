package com.infinityworks.pdfgen;

import com.google.common.base.Stopwatch;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/pdf")
@SpringBootApplication
public class App {

    public static void main(String... args) {
        SpringApplication.run(App.class, args);
    }

    @RequestMapping(method = RequestMethod.GET, produces = "application/pdf")
    public ResponseEntity<byte[]> pdf() throws Exception {
        List<ElectorRow> rows = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            rows.add(new ElectorRow("1", "Kirk, C", "ANB/31/938"));
        }

        Stopwatch stopWatch = Stopwatch.createStarted();
        CanvassCardGenerator canvassCardGenerator = new CanvassCardGenerator();
        ByteArrayOutputStream pdf = canvassCardGenerator.generatePdfTable(rows);
        System.out.println(stopWatch);
        return ResponseEntity.ok(pdf.toByteArray());
    }
}
