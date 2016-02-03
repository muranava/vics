package com.infinityworks.webapp.repository;

import com.infinityworks.webapp.domain.Ward;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface WardRepository extends JpaRepository<Ward, UUID> {
    @Query(nativeQuery = true, value =
            "SELECT * FROM wards AS w " +
                    "WHERE UPPER(w.constituency_name) = :constituencyName " +
                    "ORDER BY w.ward_name ASC")
    List<Ward> findByConstituencyName(@Param("constituencyName") String constituencyName);

    @Query(nativeQuery = true, value =
            "SELECT * FROM wards AS w " +
                    "WHERE UPPER(w.constituency_name) = :constituencyName " +
                    "AND UPPER(w.ward_name) IN :wardNames " +
                    "ORDER BY w.ward_name ASC")
    List<Ward> findByConstituencyNameAndWardNames(@Param("constituencyName") String constituencyName,
                                                  @Param("wardNames") List<String> wardNames);
}
