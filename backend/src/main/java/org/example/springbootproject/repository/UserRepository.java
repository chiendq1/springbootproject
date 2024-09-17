package org.example.springbootproject.repository;

import org.example.springbootproject.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    User findUserById(int id);

    User findUserByUsername(String username);

    User findUserByEmail(String email);

    @Query("SELECT CASE WHEN COUNT(u) > 0 THEN TRUE ELSE FALSE END FROM User u WHERE u.email = :fieldValue")
    boolean existsEmail(@Param("fieldValue") String fieldValue);

    @Query("SELECT CASE WHEN COUNT(u) > 0 THEN TRUE ELSE FALSE END FROM User u WHERE u.email = :fieldValue " +
            "AND (:id IS NOT NULL AND u.id != :id)")
    boolean duplicateEmail(@Param("fieldValue") String fieldValue, @Param("id") Integer id);

    @Query("SELECT CASE WHEN COUNT(u) > 0 THEN TRUE ELSE FALSE END FROM User u WHERE u.phoneNumber = :fieldValue " +
            "AND (:id IS NOT NULL AND u.id != :id)")
    boolean duplicatePhone(@Param("fieldValue") String fieldValue, @Param("id") Integer id);

}
