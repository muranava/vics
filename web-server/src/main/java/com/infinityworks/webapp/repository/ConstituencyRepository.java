package com.infinityworks.webapp.repository;

import com.infinityworks.webapp.domain.Constituency;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ConstituencyRepository extends JpaRepository<Constituency, UUID> {
    List<Constituency> findByNameIgnoreCaseContainingOrderByNameAsc(String name, Pageable pageable);
}
