package org.example.springbootproject.mapper;

import org.example.springbootproject.dto.BillDetailsDto;
import org.example.springbootproject.entity.BillDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class BillDetailsMapper {

    @Autowired
    private BillMapper billMapper;

    @Autowired
    private UtilityMapper utilityMapper;

    public BillDetailsDto toBillDetailsDto(BillDetails billDetails) {
        BillDetailsDto billDetailsDto = new BillDetailsDto();
        billDetailsDto.setId(billDetails.getId());
        billDetailsDto.setAmount(billDetails.getAmount());
        billDetailsDto.setUtility(utilityMapper.toDto(billDetails.getUtility()));

        return billDetailsDto;
    }
}
