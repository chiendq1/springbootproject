package org.example.springbootproject.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Date;

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
}
