package org.example.springbootproject.repository;

import org.example.springbootproject.entity.Room;
import org.example.springbootproject.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoomRepository extends JpaRepository<Room, Integer> {

    @Query("SELECT r FROM Room r JOIN r.landlord u " +
            "WHERE (:isAdmin = true OR r.landlord = :currentUser) " +  // Filter by landlord if user is not admin
            "AND (:isTenant = false OR :currentUser MEMBER OF r.roomsTenants) " + // Filter by tenant relationship
            "AND (:searchValue IS NULL OR :searchValue = '' OR " +
            "LOWER(r.roomCode) LIKE LOWER(CONCAT('%', :searchValue, '%')) OR " + // Filter by roomCode search value
            "LOWER(u.fullName) LIKE LOWER(CONCAT('%', :searchValue, '%'))) " +   // Filter by landlord fullName search value
            "AND ((:minPrice = 0 AND :maxPrice = 0) OR (r.rentPrice BETWEEN :minPrice AND :maxPrice)) " +  // Filter by price range
            "AND ((:minCapacity = 0 AND :maxCapacity = 0) OR (r.capacity BETWEEN :minCapacity AND :maxCapacity)) " + // Filter by capacity range
            "AND ((:minArea = 0 AND :maxArea = 0) OR (r.area BETWEEN :minArea AND :maxArea)) " +  // Filter by area range
            "AND (:status IS NULL OR r.status IN :status)")  // Filter by status
    Page<Room> getListRooms(
            @Param("currentUser") User currentUser,
            @Param("isAdmin") boolean isAdmin,
            @Param("isTenant") boolean isTenant,
            @Param("searchValue") String searchValue,
            @Param("minPrice") float minPrice,
            @Param("maxPrice") float maxPrice,
            @Param("minCapacity") int minCapacity,
            @Param("maxCapacity") int maxCapacity,
            @Param("minArea") float minArea,
            @Param("maxArea") float maxArea,
            @Param("status") int[] status,
            Pageable pageable
    );

    Room getRoomByRoomId(int id);

    @Query("SELECT CASE WHEN COUNT(r) > 0 THEN TRUE ELSE FALSE END FROM Room r WHERE r.roomCode = :fieldValue " +
            "AND (:id IS NOT NULL AND r.roomId != :id)")
    boolean duplicateRoomCode(@Param("fieldValue") String fieldValue, @Param("id") Integer id);
    boolean existsRoomByRoomCode(String roomCode);
}
