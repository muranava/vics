package com.infinityworks.webapp.repository;

import com.infinityworks.webapp.domain.Constituency;
import com.infinityworks.webapp.domain.Ward;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Repository
public interface WardRepository extends JpaRepository<Ward, UUID> {

    List<Ward> findByConstituencyOrderByNameAsc(Constituency constituency);

    List<Ward> findByCodeOrderByNameAsc(String code);

    Optional<Ward> findByCode(String code);

    Set<Ward> findByConstituencyIn(Set<Constituency> constituencies);

    @Query("SELECT w from Ward w LEFT JOIN w.constituency c " +
            "WHERE c = ?1 AND w.name like CONCAT(?2, '%')")
    List<Ward> findByConstituencyAndNameLike(Constituency constituency, String wardName);

    @Query(nativeQuery = true, value =
            "SELECT w.* FROM users_constituencies uc " +
            "LEFT JOIN users u ON u.id = uc.users_id " +
            "LEFT JOIN constituencies c ON c.id = uc.constituencies_id " +
            "LEFT JOIN wards w ON w.constituency_id = c.id " +
            "WHERE u.username = :username AND UPPER(w.name) LIKE %:wardName% " +
            "UNION " +
            "SELECT ww.* FROM users_wards uw " +
            "LEFT JOIN users uu ON uu.id = uw.users_id " +
            "LEFT JOIN wards ww ON ww.id = uw.wards_id " +
            "WHERE uu.username = :username AND UPPER(ww.name) LIKE %:wardName% " +
            "LIMIT :limit")
    Set<Ward> searchByUsernameAndWardName(
            @Param("username") String username,
            @Param("wardName") String wardName,
            @Param("limit") int limit);

    List<Ward> findByNameIgnoreCaseContainingOrderByNameAsc(String name, Pageable pageable);
}
