package org.example.springbootproject.repository;

import org.example.springbootproject.entity.Bill;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.sql.Date;
import java.util.List;

public interface BillRepository extends JpaRepository<Bill, Integer> {

    @Query("""
    SELECT b FROM Bill b
    JOIN b.room r
    LEFT JOIN r.landlord l
    WHERE 
        (:isAdmin = TRUE
        OR (:isLandlord = TRUE AND r.landlord.id = :userId)
        OR (:isTenant = TRUE AND EXISTS (SELECT 1 FROM r.roomsTenants t WHERE t.id = :userId)))
    AND (
        :searchValue IS NULL OR :searchValue = '' OR 
        b.billCode LIKE %:searchValue% OR
        r.roomCode LIKE %:searchValue% OR
        l.fullName LIKE %:searchValue% OR
        EXISTS (SELECT 1 FROM r.roomsTenants t WHERE t.fullName LIKE %:searchValue%)
    )
    AND (:month IS NULL OR (YEAR(b.month) = YEAR(:month) AND MONTH(b.month) = MONTH(:month)))
    AND (:roomId IS NULL OR r.roomId = :roomId)
    AND (:status IS NULL OR b.paymentStatus = :status)
""")
    Page<Bill> getListBills(
            @Param("userId") int userId,
            @Param("isAdmin") boolean isAdmin,
            @Param("isLandlord") boolean isLandlord,
            @Param("isTenant") boolean isTenant,
            @Param("searchValue") String searchValue,
            @Param("month") Date month,
            @Param("roomId") Integer roomId,
            @Param("status") Integer status,  // Keep this parameter here
            Pageable pageable
    );

    Bill findById(int id);

    @Query("""
                SELECT CASE WHEN COUNT(b) > 0 THEN TRUE ELSE FALSE END
                FROM Bill b
                WHERE b.billCode = :billCode
                AND (
                :userName = '' OR b.room.landlord.username = :userName
                )
            """)
    boolean checkBillCodeExist(
            @Param("billCode") String billCode,
            @Param("userName") String userName
    );
    @Query("""
       SELECT CASE WHEN COUNT(b) > 0 THEN TRUE ELSE FALSE END
       FROM Bill b
       WHERE b.id != :currentBillId
       AND b.room.roomId = :roomId
       AND YEAR(b.month) = YEAR(:month)
       AND MONTH(b.month) = MONTH(:month)
       AND DAY(b.month) < DAY(:month)
       """)
    boolean checkExistEarlierBillByRoomIdAndMonth(
            @Param("roomId") int roomId,
            @Param("currentBillId") int currentBillId,
            @Param("month") Date month
    );


    @Query("""
        SELECT b
        FROM Bill b
        WHERE b.paymentStatus = :status
        AND DATEDIFF(CURRENT_DATE(), b.createAt) > :overdueDate
    """)
    List<Bill> getListBillOverDaysUnpaid(@Param("status") int paymentStatus, @Param("overdueDate") int overdueDate);
}
