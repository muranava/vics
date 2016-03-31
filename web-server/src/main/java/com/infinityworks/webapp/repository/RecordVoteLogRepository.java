package com.infinityworks.webapp.repository;

import com.infinityworks.webapp.domain.RecordVoteLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface RecordVoteLogRepository extends JpaRepository<RecordVoteLog, UUID> {
}
