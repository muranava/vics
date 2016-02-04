package com.infinityworks.webapp.repository;

import com.infinityworks.webapp.domain.ElectorWithAddress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface EnrichedElectorRepository extends JpaRepository<ElectorWithAddress, UUID> {
    List<ElectorWithAddress> findByWardCodeInOrderByWardCodeAscStreetAscHouseAsc(List<String> wardCodes);
}
