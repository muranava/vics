package com.infinityworks.webapp.repository;

import com.infinityworks.webapp.domain.Elector;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ElectorRepository extends JpaRepository<Elector, UUID> {
    List<Elector> findByWardCodeIn(List<String> wardCodes);
}
