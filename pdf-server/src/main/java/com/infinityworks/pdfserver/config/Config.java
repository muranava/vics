package com.infinityworks.pdfserver.config;

import com.google.common.io.Resources;
import com.infinityworks.pafclient.PafRequestExecutor;
import com.infinityworks.pdfserver.pdf.CanvassTableConfig;
import com.infinityworks.pdfserver.pdf.DocumentBuilder;
import com.infinityworks.pdfserver.pdf.GotvTableConfig;
import com.infinityworks.pdfserver.pdf.TableBuilder;
import com.infinityworks.pdfserver.pdf.renderer.LogoRenderer;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.URL;

@Configuration
public class Config {
    @Bean
    @Qualifier("canvass")
    public TableBuilder canvassTableBuilder() {
        return new TableBuilder(new CanvassTableConfig());
    }

    @Bean
    @Qualifier("gotv")
    public TableBuilder gotvTableBuilder() {
        return new TableBuilder(new GotvTableConfig());
    }

    @Bean
    @Qualifier("canvass")
    public DocumentBuilder canvassDocumentBuilder(LogoRenderer logoRenderer) {
        return new DocumentBuilder(logoRenderer, new CanvassTableConfig());
    }

    @Bean
    @Qualifier("gotv")
    public DocumentBuilder gotvDocumentBuilder(LogoRenderer logoRenderer) {
        return new DocumentBuilder(logoRenderer, new GotvTableConfig());
    }

    @Bean
    public LogoRenderer logoRenderer() {
        URL logo = Resources.getResource("logo.png");
        return new LogoRenderer(logo);
    }

    @Bean
    public PafRequestExecutor pafRequestExecutor() {
        return new PafRequestExecutor();
    }
}
