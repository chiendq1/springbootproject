package org.example.springbootproject.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.Date;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class BillDto {
    private int id;
    private Date date;
    private String billCode;
    private RoomDto room;
    private int paymentStatus;
    private Set<BillDetailsDto> billDetails;
}
