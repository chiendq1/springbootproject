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
    public int id;
    public String enName;
    public String jaName;
    public float unitPrice;
}
