package org.example.springbootproject.repository;

import org.example.springbootproject.entity.Contract;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.sql.Date;
import java.util.List;

public interface ContractRepository extends JpaRepository<Contract, Integer> {

    @Query("""
    SELECT c FROM Contract c
    JOIN c.room r
    LEFT JOIN r.landlord l
    WHERE 
        (:isAdmin = TRUE
        OR (:isLandlord = TRUE AND r.landlord.id = :userId)
        OR (:isTenant = TRUE AND EXISTS (SELECT 1 FROM r.roomsTenants t WHERE t.id = :userId)))
    AND (
        :searchValue IS NULL OR :searchValue = '' OR 
        c.contractName LIKE %:searchValue% OR
        r.roomCode LIKE %:searchValue% OR
        l.fullName LIKE %:searchValue% OR
        EXISTS (SELECT 1 FROM r.roomsTenants t WHERE t.fullName LIKE %:searchValue%)
    )
    AND (:startDate IS NULL OR c.startDate >= :startDate)
    AND (:endDate IS NULL OR c.endDate <= :endDate)
    AND (:roomId IS NULL OR r.roomId = :roomId)
    AND (:type IS NULL OR c.type = :type)
    AND (:landlordId IS NULL OR r.landlord.id = :landlordId)
    AND (:tenant IS NULL OR EXISTS (SELECT 1 FROM r.roomsTenants t WHERE t.id = :tenant))
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

    Contract getContractById(int id);

    @Query("""
                SELECT CASE WHEN COUNT(c) > 0 THEN TRUE ELSE FALSE END
                FROM Contract c
                WHERE c.contractName = :contractName
                AND (
                :userName = '' OR c.room.landlord.username = :userName
                )
            """)
    boolean existsContractByContractName(@Param("contractName") String contractName, @Param("userName") String userName);

    @Query("""
    SELECT CASE WHEN COUNT(c) > 0 THEN TRUE ELSE FALSE END
    FROM Contract c
    WHERE c.room.roomId = :roomId
    AND :date BETWEEN c.startDate AND c.endDate
""")
    boolean existsContractByDateWithinRange(
            @Param("date") Date date,
            @Param("roomId") int roomId
    );

    Contract getContractsByStatusAndRoom_RoomId(int status, int roomId);

    List<Contract> getContractsByEndDateBeforeAndStatusNot(Date endDate, int status);

    List<Contract> getContractsByEndDateBetweenAndStatus(Date endDate, Date endDate2, int status);
}
