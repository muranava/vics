package com.infinityworks.pdfserver.config;

import com.google.common.io.Resources;
import com.infinityworks.pafclient.PafRequestExecutor;
import com.infinityworks.pdfserver.pdf.*;
import com.infinityworks.pdfserver.pdf.renderer.LogoRenderer;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Config {
    @Bean
    public CanvassTableBuilder canvassTableBuilder() {
        return new CanvassTableBuilder(new CanvassTableConfig());
    }

    @Bean
    public GotvTableBuilder gotvTableBuilder() {
        return new GotvTableBuilder(new GotvTableConfig());
    }

    @Bean
    @Qualifier("canvass")
    public DocumentBuilder canvassDocumentBuilder() {
        LogoRenderer logoRenderer = new LogoRenderer(Resources.getResource("logo.png"), 33, 515);
        String coverPageFile = "pdf/canvass_cover.pdf";
        return new DocumentBuilder(logoRenderer, new CanvassTableConfig(), coverPageFile);
    }

    @Bean
    @Qualifier("gotv")
    public DocumentBuilder gotvDocumentBuilder() {
        LogoRenderer logoRenderer = new LogoRenderer(Resources.getResource("logo.png"), 33, 760);
        String coverPageFile = "pdf/gotv_cover.pdf";
        return new DocumentBuilder(logoRenderer, new GotvTableConfig(), coverPageFile);
    }

    @Bean
    public PafRequestExecutor pafRequestExecutor() {
        return new PafRequestExecutor();
    }
}
