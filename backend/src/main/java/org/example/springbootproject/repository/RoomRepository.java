package org.example.springbootproject.repository;

import org.example.springbootproject.entity.Room;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoomRepository extends JpaRepository<Room, Integer> {

    @Query("SELECT r FROM Room r " +
            "WHERE (" +
            "(:isAdmin = true) OR " +  // Admin gets all rooms
            "(:isLandlord = true AND r.landlord.id = :userId) OR " +  // Landlord gets only their rooms
            "(:isTenant = true AND :userId IN (SELECT t.id FROM r.roomsTenants t))" +  // Tenant gets rooms they belong to
            ") " +  // This ends the role-based room access logic
            "AND (" +
            "(:searchValue IS NULL OR :searchValue = '' OR " +
            "LOWER(r.roomCode) LIKE LOWER(CONCAT('%', :searchValue, '%')) OR " +  // Filter by roomCode search value
            "LOWER(r.landlord.fullName) LIKE LOWER(CONCAT('%', :searchValue, '%'))) " +   // Filter by landlord fullName search value
            ") " +
            "AND ((:minPrice = 0 AND :maxPrice = 0) OR (r.rentPrice BETWEEN :minPrice AND :maxPrice)) " +  // Filter by price range
            "AND ((:minCapacity = 0 AND :maxCapacity = 0) OR (r.capacity BETWEEN :minCapacity AND :maxCapacity)) " +  // Filter by capacity range
            "AND ((:minArea = 0 AND :maxArea = 0) OR (r.area BETWEEN :minArea AND :maxArea)) " +  // Filter by area range
            "AND (:status IS NULL OR r.status IN :status)"  // Filter by status
    )
    Page<Room> getListRooms(
            @Param("userId") int userId,
            @Param("isAdmin") boolean isAdmin,
            @Param("isLandlord") boolean isLandlord,
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

    @Query("SELECT CASE WHEN COUNT(r) > 0 THEN TRUE ELSE FALSE END FROM Room r " +
            "WHERE r.roomCode = :fieldValue " +
            "AND (:id IS NULL OR r.roomId != :id) " +  // Exclude the room with the given id
            "AND r.landlord.id = :landlordId")     // Filter by landlord's name
    boolean duplicateRoomCode(@Param("fieldValue") String fieldValue,
                              @Param("id") Integer id,
                              @Param("landlordId") int landlordId);


    @Query("SELECT CASE WHEN COUNT(r) > 0 THEN TRUE ELSE FALSE END FROM Room r " +
            "WHERE r.roomCode = :fieldValue " +
            "AND r.landlord.id = :landlordId")  // Filter by landlord's name
    boolean existsRoomByRoomCode(@Param("fieldValue") String roomCode,
                                 @Param("landlordId") int landlordId);


    @Query("SELECT r FROM Room r " +
            "LEFT JOIN r.contracts c " +
            "WHERE (:getByContract = false OR (r.status = 1))" +
            "AND (:highestRole = 'ADMIN') " +
            "OR (:highestRole = 'LANDLORD' AND r.landlord.id = :userId) " +
            "OR (:highestRole = 'TENANT' AND :userId IN (SELECT t.id FROM r.roomsTenants t)) "
    )
    List<Room> getListRoomsByRole(
            @Param("highestRole") String highestRole,
            @Param("userId") int userId,
            @Param("getByContract") boolean getByContract
    );
}
