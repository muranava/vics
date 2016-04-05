package com.infinityworks.webapp.repository;

import com.infinityworks.webapp.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
    Optional<User> findOneByUsername(String username);

    @Query(nativeQuery = true,
           value = "SELECT username, first_name, last_name, write_access, role, cast(id as text) FROM users ORDER BY username")
    List<Object[]> allUserSummaries();
}
