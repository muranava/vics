package com.infinityworks.canvass.pafstub;

import java.io.IOException;
import java.util.concurrent.Executors;

/**
 * App for running a stub PAF api (for demo purposes only)
 */
public class PafServerRunner {
    static Runnable pathServer = () -> {
        PafServerStub pafApiStub = new PafServerStub(9002);
        pafApiStub.start();
        try {
            pafApiStub.willReturnStreets();
            pafApiStub.willReturnVotersByStreets();
            pafApiStub.willRecordVoterVoted();
        } catch (IOException e) {
            throw new IllegalStateException("Could not start PAF mock server");
        }
    };

    public static void main(String... args) {
        Executors.newSingleThreadExecutor().execute(pathServer);
    }
}
