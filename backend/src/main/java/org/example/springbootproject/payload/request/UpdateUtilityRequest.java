package org.example.springbootproject.payload.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.example.springbootproject.utils.Constants;
import org.example.springbootproject.validation.UtilityNameExist;

@Data
@Getter
@Setter
public class UpdateUtilityRequest {

    @NotBlank(message = "{common.required}")
    private String name;

    @Min(value = Constants.MIN_SIZE_NUMBER, message = "{common.number_min}")
    @Max(value = Constants.MAX_SIZE_NUMBER, message = "{common.number_max}")
    private int unitPrice;

    @NotBlank(message = "{common.required}")
    private String unit;
}
