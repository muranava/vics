package com.infinityworks.webapp.testsupport.stub;

import java.util.concurrent.Executors;

/**
 * App for running a stub PAF api (for demo purposes only)
 */
public class PafServerRunner {
    static Runnable pathServer = () -> {
        PafServer pafApiStub = new PafServer();
        pafApiStub.start();
        pafApiStub.willReturnPafForWard("E09");
    };

    public static void main(String... args) {
        Executors.newSingleThreadExecutor().execute(pathServer);
    }
}
