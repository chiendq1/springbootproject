package org.example.springbootproject.mapper;

import org.example.springbootproject.dto.UtilityDto;
import org.example.springbootproject.entity.Utility;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
public class UtilityMapper {

    public UtilityDto toDto(Utility utility) {
        UtilityDto utilityDto = new UtilityDto();
        utilityDto.setId(utility.getId());
        utilityDto.setJaName(utility.getJaName());
        utilityDto.setEnName(utility.getEnName());
        utilityDto.setUnitPrice(utility.getUnitPrice());
        utilityDto.setUnit(utility.getUnit());
        utilityDto.setStatus(utility.getStatus());

        return utilityDto;
    }

    public Set<UtilityDto> toDtoSet(Set<Utility> utilities) {
        Set<UtilityDto> utilityDtos = new HashSet<>();
        for (Utility utility : utilities) {
            utilityDtos.add(toDto(utility));
        }

        return utilityDtos;
    }

    public List<UtilityDto> toDtoList(List<Utility> utilities) {
        List<UtilityDto> utilityDtos = new ArrayList<>();
        for (Utility utility : utilities) {
            utilityDtos.add(toDto(utility));
        }

        return utilityDtos;
    }
}
