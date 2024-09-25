package org.example.springbootproject.repository;

import org.example.springbootproject.entity.Contract;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.sql.Date;

public interface ContractRepository extends JpaRepository<Contract, Integer> {

    @Query("""
    SELECT c FROM Contract c
    JOIN c.room r
    LEFT JOIN r.landlord l
    LEFT JOIN r.roomsTenants t
    WHERE 
        (:isAdmin = TRUE
        OR (:isLandlord = TRUE AND r.landlord.id = :userId)
        OR (:isTenant = TRUE AND t.id = :userId))
    AND (
        :searchValue IS NULL OR :searchValue = '' OR 
        c.contractName LIKE %:searchValue% OR
        r.roomCode LIKE %:searchValue% OR
        l.fullName LIKE %:searchValue% OR
        t.fullName LIKE %:searchValue%
    )
    AND (:startDate IS NULL OR c.startDate >= :startDate)
    AND (:endDate IS NULL OR c.endDate <= :endDate)
    AND (:roomId IS NULL OR r.roomId = :roomId)
    AND (:type IS NULL OR c.type = :type)
    AND (:landlordId IS NULL OR r.landlord.id = :landlordId)
    AND (:tenant IS NULL OR t.id = :tenant)
    AND (:status IS NULL OR c.status = :status)
""")
    Page<Contract> getListContracts(
            @Param("userId") int userId,
            @Param("isAdmin") boolean isAdmin,
            @Param("isLandlord") boolean isLandlord,
            @Param("isTenant") boolean isTenant,
            @Param("searchValue") String searchValue,
            @Param("startDate") Date startDate,
            @Param("endDate") Date endDate,
            @Param("roomId") Integer roomId,
            @Param("status") Integer status,  // Keep this parameter here
            @Param("tenant") Integer tenant,
            @Param("landlordId") Integer landLordId,
            @Param("type") Integer type,
            Pageable pageable
    );
}
