package org.example.springbootproject.service;

import org.example.springbootproject.dto.RoomDto;
import org.example.springbootproject.dto.UserDto;
import org.example.springbootproject.entity.Room;
import org.example.springbootproject.entity.User;
import org.example.springbootproject.mapper.RoomMapper;
import org.example.springbootproject.payload.request.GetRoomListRequest;
import org.example.springbootproject.repository.RoomRepository;
import org.example.springbootproject.repository.UserRepository;
import org.example.springbootproject.utils.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class RoomService extends BaseService{

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private UserRepository userRepository;

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
}
