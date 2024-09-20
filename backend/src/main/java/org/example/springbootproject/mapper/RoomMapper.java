package org.example.springbootproject.mapper;

import org.example.springbootproject.dto.RoomDto;
import org.example.springbootproject.entity.Room;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class RoomMapper {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UtilityMapper utilityMapper;

    public RoomDto toRoomDto(Room room) {
        RoomDto roomDto = new RoomDto();
        roomDto.setRoomCode(room.getRoomCode());
        roomDto.setRoomId(room.getRoomId());
        roomDto.setArea(room.getArea());
        roomDto.setCapacity(room.getCapacity());
        roomDto.setRentPrice(room.getRentPrice());
        roomDto.setRoomsTenants(room.getRoomsTenants().stream().map(
                user -> userMapper.toDTO(user)
        ).collect(Collectors.toSet()));
        roomDto.setLandlord(userMapper.toDTO(room.getLandlord()));
        roomDto.setStatus(room.getStatus());
        roomDto.setUtilities(room.getUtilities().stream().map(
                utility -> utilityMapper.toDto(utility)
        ).collect(Collectors.toSet()));

        return roomDto;
    }
}
