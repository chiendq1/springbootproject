package org.example.springbootproject.repository;

import org.example.springbootproject.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    User findUserById(int id);

    User findUserByUsername(String username);

    User findUserByEmail(String email);

    void deleteUserById(int id);

    @Query("SELECT CASE WHEN COUNT(u) > 0 THEN TRUE ELSE FALSE END FROM User u WHERE u.email = :fieldValue")
    boolean existsEmail(@Param("fieldValue") String fieldValue);

    @Query("SELECT CASE WHEN COUNT(u) > 0 THEN TRUE ELSE FALSE END FROM User u WHERE u.phoneNumber = :fieldValue")
    boolean existsPhoneNumber(@Param("fieldValue") String fieldValue);

    @Query("SELECT CASE WHEN COUNT(u) > 0 THEN TRUE ELSE FALSE END FROM User u WHERE u.username = :fieldValue")
    boolean existsUserName(@Param("fieldValue") String fieldValue);

    @Query("SELECT CASE WHEN COUNT(u) > 0 THEN TRUE ELSE FALSE END FROM User u WHERE u.email = :fieldValue " +
            "AND (:id IS NOT NULL AND u.id != :id)")
    boolean duplicateEmail(@Param("fieldValue") String fieldValue, @Param("id") Integer id);

    @Query("SELECT CASE WHEN COUNT(u) > 0 THEN TRUE ELSE FALSE END FROM User u WHERE u.phoneNumber = :fieldValue " +
            "AND (:id IS NOT NULL AND u.id != :id)")
    boolean duplicatePhone(@Param("fieldValue") String fieldValue, @Param("id") Integer id);

    @Query("SELECT CASE WHEN COUNT(u) > 0 THEN TRUE ELSE FALSE END FROM User u WHERE u.username = :fieldValue " +
            "AND (:id IS NOT NULL AND u.id != :id)")
    boolean duplicateUsername(@Param("fieldValue") String fieldValue, @Param("id") Integer id);

    @Query("SELECT u FROM User u JOIN u.roles r " +
            "WHERE (:searchValue IS NULL OR " +
            "LOWER(u.fullName) LIKE LOWER(CONCAT('%', :searchValue, '%')) OR " +
            "LOWER(u.email) LIKE LOWER(CONCAT('%', :searchValue, '%')) OR " +
            "LOWER(u.location) LIKE LOWER(CONCAT('%', :searchValue, '%')) OR " +
            "LOWER(u.username) LIKE LOWER(CONCAT('%', :searchValue, '%')) OR " +
            "LOWER(u.phoneNumber) LIKE LOWER(CONCAT('%', :searchValue, '%'))) " +
            "AND (:filterValue IS NULL OR :filterValue = '' OR LOWER(r.roleName) = LOWER(:filterValue)) " +
            "AND 'ADMIN' NOT IN (SELECT LOWER(r2.roleName) FROM u.roles r2)")
    Page<User> getUsersBySearchAndFilter(@Param("searchValue") String searchValue,
                                         @Param("filterValue") String filterValue,
                                         Pageable pageable);

    @Query("""
    SELECT u
    FROM User u
    LEFT JOIN u.roles r
    LEFT JOIN u.roomsTenants rt
    LEFT JOIN rt.landlord rl
    WHERE 
        (:highestRole = 'ADMIN' AND u.id <> :userId)
        
        OR (:highestRole = 'LANDLORD' 
            AND (
                (u.id = :userId) 
                OR (rl.id = :userId)
            )
        )
        
        OR (:highestRole = 'TENANT' 
            AND (
                (u.id = :userId)
                OR (u.id = rl.id)
            )
        )
        
        AND NOT EXISTS (
            SELECT 1 
            FROM u.roles r2 
            WHERE r2.roleName = 'ADMIN'
        )
    """)
    List<User> getListUsersByRole(
            @Param("highestRole") String highestRole,
            @Param("userId") Integer userId
    );

    @Query("SELECT u FROM User u " +
            "JOIN u.roles r " +
            "WHERE r.roleName = 'TENANT' " +
            "AND u.roomsTenants IS EMPTY")
    List<User> getListFreeTenants();

    Set<User> getUsersByIdIn(Set<Integer> ids);
}
