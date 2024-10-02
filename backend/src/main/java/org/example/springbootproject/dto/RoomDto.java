package org.example.springbootproject.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RoomDto {
    private int roomId;
    private String roomCode;
    private float area;
    private int capacity;
    private float rentPrice;
    private int status;
    private UserDto landlord;
    private Set<UtilityDto> utilities;
    private Set<UserDto> roomsTenants;
    private Set<BillDto> bills;
    private Set<ContractDto> contracts;
}
