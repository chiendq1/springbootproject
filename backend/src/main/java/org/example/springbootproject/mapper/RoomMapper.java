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

    @Autowired
    private ContractMapper contractMapper;

    @Autowired
    private BillMapper billMapper;

    public RoomDto toRoomDto(Room room, boolean isGetBill) {
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
        roomDto.setUtilities(utilityMapper.toDtoSet(room.getUtilities()));
        if(isGetBill) roomDto.setBills(billMapper.toBillDtoSetForRoomDetails(room.getBills()));
        roomDto.setContracts(room.getContracts().stream().map(
                contract -> contractMapper.toContractDtoWithoutRoom(contract)
        ).collect(Collectors.toSet()));
        return roomDto;
    }
}
