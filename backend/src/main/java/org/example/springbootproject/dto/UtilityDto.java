package org.example.springbootproject.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UtilityDto {
    private int id;
    private String enName;
    private String jaName;
    private float unitPrice;
    private String unit;
    private int status;
}
