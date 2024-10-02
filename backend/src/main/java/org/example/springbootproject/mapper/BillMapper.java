package org.example.springbootproject.mapper;

import lombok.extern.slf4j.Slf4j;
import org.example.springbootproject.controller.BaseController;
import org.example.springbootproject.dto.BillDto;
import org.example.springbootproject.dto.UtilityDto;
import org.example.springbootproject.entity.Bill;
import org.example.springbootproject.entity.Utility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class BillMapper {

    @Autowired
    private BillDetailsMapper billDetailsMapper;

    @Autowired
    private RoomMapper roomMapper;

    public BillDto toBillDtoForRoomDetails(Bill bill) {
        BillDto billDto = new BillDto();
        billDto.setBillDetails(
                bill.getBillDetails().stream()
                        .map(billDetails -> billDetailsMapper.toBillDetailsDto(billDetails))
                        .collect(Collectors.toSet())
        );
        billDto.setId(bill.getId());
        billDto.setPaymentStatus(bill.getPaymentStatus());
        billDto.setDate(bill.getMonth());

        return billDto;
    }

    public Set<BillDto> toBillDtoSetForRoomDetails(Set<Bill> billSet) {
        Set<BillDto> billDtos = new HashSet<>();
        for (Bill bill : billSet) {
            billDtos.add(toBillDtoForRoomDetails(bill));
        }

        return billDtos;
    }

    public BillDto toBillDto(Bill bill) {
        BillDto billDto = new BillDto();
        billDto.setId(bill.getId());
        billDto.setBillCode(bill.getBillCode());
        billDto.setRoom(roomMapper.toRoomDto(bill.getRoom(), true));
        billDto.setPaymentStatus(bill.getPaymentStatus());
        billDto.setDate(bill.getMonth());
        billDto.setBillDetails(
                bill.getBillDetails().stream()
                        .map(billDetails -> billDetailsMapper.toBillDetailsDto(billDetails))
                        .collect(Collectors.toSet())
        );

        return billDto;
    }
}
