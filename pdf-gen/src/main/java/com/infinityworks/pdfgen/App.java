package com.infinityworks.pdfgen;

import com.google.common.base.Stopwatch;
import com.itextpdf.text.Document;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
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
//        for (int i = 0; i < 15; i++) {
//            rows.add(new ElectorRow("1", "Main Street", "Kirk, C", "07989313444", "", "", "", "", "", "", "", "", "", "", "S13002629/31/938"));
//        }
//        for (int i = 0; i < 2; i++) {
//            rows.add(new ElectorRow("2", "Main Street", "Adam, V", "07989313444", "", "", "", "", "", "", "", "", "", "", "S13002629/31/938"));
//        }
//        for (int i = 0; i < 1; i++) {
//            rows.add(new ElectorRow("5", "Main Street", "Smith, V", "07989313444", "", "", "", "", "", "", "", "", "", "", "S13002629/31/938"));
//        }
//        for (int i = 0; i < 4; i++) {
//            rows.add(new ElectorRow("Grange Farm House", "Abingdon Road", "Fletcher, V", "07989313444", "", "", "", "", "", "", "", "", "", "", "S13002629/31/938"));
//        }

        Stopwatch stopWatch = Stopwatch.createStarted();
        TableBuilder tableBuilder = new TableBuilder("", "", "");
        PdfPTable pdfTable = tableBuilder.generateTableRows(rows);
        ByteArrayOutputStream os = new ByteArrayOutputStream();

        Document document = new Document(PageSize.A4.rotate(), 30, 10, 100, 30);
        PdfWriter writer = PdfWriter.getInstance(document, os);
        StaticContentRenderer event = new StaticContentRenderer();
        writer.setPageEvent(event);

        document.open();
        document.add(pdfTable);
        document.newPage();
        document.add(pdfTable);
        document.close();

        System.out.println(stopWatch);
        return ResponseEntity.ok(os.toByteArray());
    }
}
