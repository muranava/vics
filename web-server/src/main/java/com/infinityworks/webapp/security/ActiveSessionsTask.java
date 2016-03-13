package com.infinityworks.webapp.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class ActiveSessionsTask {

    private final Logger log = LoggerFactory.getLogger(ActiveSessionsTask.class);

//    @Scheduled(fixedRate = 1000 * 60 * 5)
    public void scheduleFixedDelayTask() {
        log.info("Scheduled...");

    }
}
