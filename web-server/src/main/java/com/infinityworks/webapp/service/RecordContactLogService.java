package com.infinityworks.webapp.service;

import com.infinityworks.webapp.domain.RecordContactLog;
import com.infinityworks.webapp.repository.RecordContactLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class RecordContactLogService {
    private final RecordContactLogRepository recordVoteLogRepository;

    @Autowired
    public RecordContactLogService(RecordContactLogRepository recordContactLogRepository) {
        this.recordVoteLogRepository = recordContactLogRepository;
    }

    @Async
    public void logRecordContactAsync(RecordContactLog logEntry) {
        recordVoteLogRepository.save(logEntry);
    }

    @Async
    public void deleteRecordContact(RecordContactLog log) {
        recordVoteLogRepository.delete(log);
    }
}
