package org.example.springbootproject.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BillServiceDto {
    private String enName;
    private String unitPrice;
    private int amount;
    private String unit;
    private String price;
    private String serviceTotalPrice;
}
