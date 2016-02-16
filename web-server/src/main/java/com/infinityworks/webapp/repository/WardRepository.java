package com.infinityworks.webapp.repository;

import com.infinityworks.webapp.domain.Constituency;
import com.infinityworks.webapp.domain.Ward;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@Repository
public interface WardRepository extends JpaRepository<Ward, UUID> {

    List<Ward> findByConstituencyOrderByNameAsc(Constituency constituency);

    List<Ward> findByCodeOrderByNameAsc(String code);

    Set<Ward> findByConstituencyIn(Set<Constituency> constituencies);

    @Query("SELECT w from Ward w LEFT JOIN w.constituency c " +
            "WHERE c = ?1 AND w.name like CONCAT(?2, '%')")
    List<Ward> findByConstituencyAndNameLike(Constituency constituency, String wardName);
}
