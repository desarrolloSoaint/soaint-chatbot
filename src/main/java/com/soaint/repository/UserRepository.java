package com.soaint.repository;

import com.soaint.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<Users, Integer>, JpaSpecificationExecutor<Users> {
    Optional<Users> findByEmail(String email);
    boolean existsByEmail(String email);

    @Query(value = "Select COUNT (email) FROM users where created_at>=current_date-7", nativeQuery = true)
    long UsersCountLastWeek();

    @Query(value = "Select COUNT (email) FROM users where created_at>=current_date-0", nativeQuery = true)
    long UsersCountLastDay();
}
