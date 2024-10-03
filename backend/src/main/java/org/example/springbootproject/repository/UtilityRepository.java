package org.example.springbootproject.repository;

import org.example.springbootproject.entity.Utility;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Set;

@Repository
public interface UtilityRepository extends JpaRepository<Utility, Integer> {
    Set<Utility> findAllByIdIn(Set<Integer> ids);

    @Query("SELECT u FROM Utility u " +
            "WHERE (:searchValue IS NULL OR :searchValue = '' OR " +
            "LOWER(u.enName) LIKE LOWER(CONCAT('%', :searchValue, '%')) OR " +
            "LOWER(u.jaName) LIKE LOWER(CONCAT('%', :searchValue, '%')))" +
            "AND (:status IS NULL OR u.status = :status)"
    )
    List<Utility> getListUtilities(
            @Param("searchValue") String searchValue,
            @Param("status") Integer status
    );

    Utility getUtilityById(Integer id);

    boolean existsUtilityByEnNameOrJaNameAndStatusNot(String enName, String jaName, int status);

    @Query("SELECT CASE WHEN COUNT(u) > 0 THEN TRUE ELSE FALSE END FROM Utility u " +
            "WHERE (u.enName = :fieldValue OR u.jaName = :fieldValue) " +
            "AND (:id IS NULL OR u.id != :id)" +
            "AND u.status != :status")
    boolean duplicateName(@Param("fieldValue") String fieldValue, @Param("id") Integer id, @Param("status") Integer status);
}
