package org.example.springbootproject.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CurrencyDto {
    private String code;
    private float rate;
}
