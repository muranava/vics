package com.infinityworks.webapp.repository;

import com.infinityworks.webapp.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface StatsRepository extends JpaRepository<User, UUID> {
    @Query(nativeQuery = true, value =
            "SELECT u.username,u.first_name,u.last_name,COUNT(DISTINCT(l.ern)) as counts " +
                    "FROM record_contact_log l " +
                    "JOIN users u ON u.id = l.users_id " +
                    "GROUP BY u.id " +
                    "ORDER BY counts DESC " +
                    "LIMIT :limit"
    )
    List<Object[]> countRecordContactByUser(@Param("limit") int limit);

    @Query(nativeQuery = true, value =
            "SELECT c.name,COUNT(DISTINCT(l.ern)) as counts " +
                    "FROM record_contact_log l " +
                    "JOIN wards w ON w.id = l.wards_id " +
                    "JOIN constituencies c ON c.id = w.constituency_id " +
                    "GROUP BY c.id " +
                    "ORDER BY counts DESC " +
                    "LIMIT :limit"
    )
    List<Object[]> countMostRecordContactByConstituency(@Param("limit") int limit);

    @Query(nativeQuery = true, value =
            "SELECT w.name,COUNT(DISTINCT(l.ern)) as counts " +
                    "FROM record_contact_log l " +
                    "JOIN wards w ON w.id = l.wards_id " +
                    "JOIN constituencies c ON c.id = w.constituency_id " +
                    "GROUP BY w.id " +
                    "ORDER BY counts DESC " +
                    "LIMIT :limit"
    )
    List<Object[]> countMostRecordContactByWard(@Param("limit") int limit);
}
