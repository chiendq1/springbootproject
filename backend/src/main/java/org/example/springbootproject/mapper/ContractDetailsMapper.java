package org.example.springbootproject.mapper;

import org.example.springbootproject.dto.ContractDetailsDto;
import org.example.springbootproject.entity.ContractDetails;
import org.springframework.stereotype.Component;

@Component
public class ContractDetailsMapper {
    public ContractDetailsDto toDto(ContractDetails contractDetails) {
        ContractDetailsDto dto = new ContractDetailsDto();
        dto.setId(contractDetails.getId());
        dto.setContractId(contractDetails.getContract().getId());
        dto.setUtilityId(contractDetails.getUtility().getId());
        dto.setUnitPrice(contractDetails.getUnitPrice());

        return dto;
    }
}
