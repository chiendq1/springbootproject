package org.example.springbootproject.mapper;

import org.example.springbootproject.dto.ContractDto;
import org.example.springbootproject.dto.RoomDto;
import org.example.springbootproject.entity.Contract;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class ContractMapper {

    @Autowired
    private RoomMapper roomMapper;

    @Autowired
    private ContractDetailsMapper contractDetailsMapper;

    public ContractDto toContractDto(Contract contract) {
        ContractDto contractDto = new ContractDto();
        contractDto.setContractName(contract.getContractName());
        contractDto.setStartDate(contract.getStartDate());
        contractDto.setEndDate(contract.getEndDate());
        contractDto.setDeposit(contract.getDeposit());
        contractDto.setType(contract.getType());
        contractDto.setId(contract.getId());
        contractDto.setStatus(contract.getStatus());
        contractDto.setRoom(roomMapper.toRoomDto(contract.getRoom(), true));
        contractDto.setContractDetails(contract.getContractDetails().stream().map(contractDetails -> contractDetailsMapper.toDto(contractDetails)).collect(Collectors.toSet()));

        return contractDto;
    }

    public ContractDto toContractDtoWithoutRoom(Contract contract) {
        ContractDto contractDto = new ContractDto();
        contractDto.setContractName(contract.getContractName());
        contractDto.setStartDate(contract.getStartDate());
        contractDto.setEndDate(contract.getEndDate());
        contractDto.setDeposit(contract.getDeposit());
        contractDto.setType(contract.getType());
        contractDto.setId(contract.getId());
        contractDto.setStatus(contract.getStatus());
        contractDto.setContractDetails(contract.getContractDetails().stream().map(contractDetails -> contractDetailsMapper.toDto(contractDetails)).collect(Collectors.toSet()));

        return contractDto;
    }
}
