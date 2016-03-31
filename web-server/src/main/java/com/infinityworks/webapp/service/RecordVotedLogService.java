package com.infinityworks.webapp.service;

import com.infinityworks.webapp.domain.RecordVoteLog;
import com.infinityworks.webapp.domain.User;
import com.infinityworks.webapp.domain.Ward;
import com.infinityworks.webapp.repository.RecordVoteLogRepository;
import com.infinityworks.webapp.rest.dto.RecordVoteRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class RecordVotedLogService {
    private final RecordVoteLogRepository recordVoteLogRepository;

    @Autowired
    public RecordVotedLogService(RecordVoteLogRepository recordVoteLogRepository) {
        this.recordVoteLogRepository = recordVoteLogRepository;
    }

    @Async
    public void logRecordVote(RecordVoteRequest recordVote, User user, Ward ward) {
        RecordVoteLog logEntry = new RecordVoteLog();
        logEntry.setErn(recordVote.getErn());
        logEntry.setUser(user);
        logEntry.setWard(ward);
        recordVoteLogRepository.save(logEntry);
    }
}
