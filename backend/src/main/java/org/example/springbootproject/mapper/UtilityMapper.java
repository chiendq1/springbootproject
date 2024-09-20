package org.example.springbootproject.mapper;

import org.example.springbootproject.dto.UtilityDto;
import org.example.springbootproject.entity.Utility;
import org.springframework.stereotype.Component;

@Component
public class UtilityMapper {

    public UtilityDto toDto(Utility utility) {
        UtilityDto utilityDto = new UtilityDto();
        utilityDto.setId(utility.getId());
        utilityDto.setJaName(utility.getJaName());
        utilityDto.setEnName(utility.getEnName());
        utilityDto.setUnitPrice(utility.getUnitPrice());

        return utilityDto;
    }
}
