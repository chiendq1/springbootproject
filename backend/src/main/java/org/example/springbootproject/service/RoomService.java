package org.example.springbootproject.service;

import org.example.springbootproject.dto.RoomDto;
import org.example.springbootproject.dto.UserDto;
import org.example.springbootproject.entity.Room;
import org.example.springbootproject.entity.User;
import org.example.springbootproject.mapper.RoomMapper;
import org.example.springbootproject.payload.request.CreateRoomRequest;
import org.example.springbootproject.payload.request.GetRoomListRequest;
import org.example.springbootproject.payload.request.RoomTenantsRequest;
import org.example.springbootproject.payload.request.UpdateRoomDetailsRequest;
import org.example.springbootproject.repository.RoomRepository;
import org.example.springbootproject.repository.UserRepository;
import org.example.springbootproject.repository.UtilityRepository;
import org.example.springbootproject.utils.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
public class RoomService extends BaseService {

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UtilityRepository utilityRepository;

    @Autowired
    private RoomMapper roomMapper;

    public Map<String, Object> getListRooms(GetRoomListRequest request, String currentUserName) {
        User currentUser = userRepository.findUserByUsername(currentUserName);
        boolean isAdmin = currentUser.getRoles().stream().anyMatch(role -> role.getRoleName().equals("ADMIN"));
        boolean isTenant = currentUser.getRoles().stream().anyMatch(role -> role.getRoleName().equals("TENANT"));

        Pageable paging = PageRequest.of(request.getPageNo(), Constants.PAGE_SIZE, Sort.by(request.getSortBy()));

        Page<Room> listRooms = roomRepository.getListRooms(
                currentUser,
                isAdmin,
                isTenant,
                request.getSearchValue(),
                request.getPriceRange()[0],
                request.getPriceRange()[1],
                request.getCapacity()[0],
                request.getCapacity()[1],
                request.getArea()[0],
                request.getArea()[1],
                request.getStatus(),
                paging
        );

        List<RoomDto> rooms = listRooms.stream()
                .map(room -> roomMapper.toRoomDto(room))
                .toList();

        Map<String, Object> response = new HashMap<>();
        response.put("rooms", rooms);
        response.put("currentPage", listRooms.getNumber());
        response.put("totalItems", listRooms.getTotalElements());
        response.put("totalPages", listRooms.getTotalPages());

        return response;
    }

    public Map<String, Object> getRoomDetails(int id) {
        Map<String, Object> response = new HashMap<>();
        Room roomDetails = roomRepository.getRoomByRoomId(id);
        response.put("roomDetails", roomMapper.toRoomDto(roomDetails));

        return response;
    }

    public Map<String, Object> getListRoomsByRole(UserDetails currentUser) {
        try {
            User currentUserEntity = userRepository.findUserByUsername(currentUser.getUsername());
            Map<String, Object> response = new HashMap<>();
            List<Room> listUsers = roomRepository.getListRoomsByRole(currentUserEntity.getHighestRole(), currentUserEntity.getId());
            List<RoomDto> rooms = listUsers.stream()
                    .map(room -> roomMapper.toRoomDto(room))
                    .toList();
            response.put("rooms", rooms);

            return response;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }

        return null;
    }

    @Transactional
    public Map<String, Object> createRoom(CreateRoomRequest request) {
        Map<String, Object> response = new HashMap<>();
        Room room = new Room();
        if(request.getCapacity() < request.getListTenants().size()) {
            response.put("errors", "E-CM-019"); // Capacity exceeded error
            return response;
        }
        room.setRoomCode(request.getRoomCode());
        room.setArea(request.getArea());
        room.setCapacity(request.getCapacity());
        room.setStatus(request.getStatus());
        room.setRentPrice(request.getRentPrice());
        room.setLandlord(userRepository.findUserById(request.getLandlordId()));
        room.setUtilities(utilityRepository.findAllByIdIn(request.getUtilities()));
        room.setRoomsTenants(userRepository.getUsersByIdIn(request.getListTenants()));

        roomRepository.save(room);
        response.put("roomDetails", room);

        return response;
    }

    @Transactional
    public Map<String, Object> updateRoomDetails(int id, UpdateRoomDetailsRequest request) {
        Map<String, Object> response = new HashMap<>();
        Room roomDetails = roomRepository.getRoomByRoomId(id);
        if (roomDetails.getStatus() == Constants.ROOM_STATUS_RENT) return null;

        roomDetails.setRoomCode(request.getRoomCode());
        roomDetails.setArea(request.getArea());
        roomDetails.setStatus(request.getStatus());
        roomDetails.setCapacity(request.getCapacity());
        roomDetails.setRentPrice(request.getRentPrice());
        roomDetails.setUtilities(utilityRepository.findAllByIdIn(request.getUtilities()));
        response.put("roomDetails", roomMapper.toRoomDto(roomDetails));

        return response;
    }

    @Transactional
    public Map<String, Object> manageTenants(int roomId, RoomTenantsRequest request, boolean isAdding) {
        Map<String, Object> response = new HashMap<>();
        try {
            Room room = roomRepository.getRoomByRoomId(roomId);
            Set<User> tenants = userRepository.getUsersByIdIn(request.getListTenants());
            Set<User> currentTenants = room.getRoomsTenants();

            if (isAdding) {
                if (tenants.size() + currentTenants.size() > room.getCapacity()) {
                    response.put("errors", "E-CM-019"); // Capacity exceeded error
                    return response;
                }

                currentTenants.addAll(tenants);
            } else {
                currentTenants.removeAll(tenants);
            }

            room.setRoomsTenants(currentTenants);
            roomRepository.save(room);
            response.put("roomDetails", roomMapper.toRoomDto(room));

            return response;

        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        }

        return null;
    }

    @Transactional
    public void deleteRoom(int id) {
        Map<String, Object> response = new HashMap<>();
        Room room = roomRepository.getRoomByRoomId(id);
        roomRepository.delete(room);
    }

    public boolean hasRoom(int id, String userName, boolean isCheckLandlord) {
        Room roomDetails = roomRepository.getRoomByRoomId(id);

        return roomDetails != null && (!roomDetails.getLandlord().getUsername().equals(userName) && isCheckLandlord || roomDetails.getRoomsTenants().stream().anyMatch(tenant -> tenant.getUsername().equals(userName)));
    }

    public String checkDuplicateField(String fieldName, String fieldValue, Integer id) {
        if (id == null) {
            if (roomRepository.existsRoomByRoomCode(fieldValue)) return "E-CM-017";
        } else {
            switch (fieldName) {
                case "roomCode" -> {
                    if (roomRepository.duplicateRoomCode(fieldValue, id)) return "E-CM-017";
                }
            }
        }

        return "";
    }
}
