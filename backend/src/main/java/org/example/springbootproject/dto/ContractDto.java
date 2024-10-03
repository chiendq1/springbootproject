package org.example.springbootproject.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.springbootproject.entity.ContractDetails;

import java.sql.Date;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ContractDto {
    private int id;
    private RoomDto room;
    private String contractName;
    private Date startDate;
    private Date endDate;
    private float deposit;
    private int type;
    private int status;
    private Set<ContractDetailsDto> contractDetails;
}
