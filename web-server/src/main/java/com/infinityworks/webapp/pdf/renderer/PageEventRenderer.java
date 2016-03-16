package com.infinityworks.webapp.pdf.renderer;

import com.lowagie.text.pdf.PdfPageEventHelper;

import javax.annotation.concurrent.NotThreadSafe;

/**
 * It is intended that each thread will create a new instance of this class
 */
@NotThreadSafe
public abstract class PageEventRenderer extends PdfPageEventHelper {
    private boolean enabled;

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}
