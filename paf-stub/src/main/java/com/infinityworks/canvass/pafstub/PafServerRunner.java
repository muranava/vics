package com.infinityworks.canvass.pafstub;

import java.io.IOException;
import java.util.concurrent.Executors;

/**
 * App for running a stub PAF api (for demo purposes only)
 */
public class PafServerRunner {
    private static Runnable pafServer = () -> {
        PafServerStub pafApiStub = new PafServerStub(9002);
        pafApiStub.start();
        try {
            pafApiStub.willReturnStreets();
            pafApiStub.willRecordVoterVoted();
            pafApiStub.willReturnVotersByWardByTownAndByStreet("E05000403", "Kingston");
            pafApiStub.willSearchVoters("E05000403", "kt25bu", "fletcher");
        } catch (IOException e) {
            throw new IllegalStateException("Could not start PAF mock server");
        }
    };

    public static void main(String... args) {
        Executors.newSingleThreadExecutor().execute(pafServer);
    }
}
