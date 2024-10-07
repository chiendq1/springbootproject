package org.example.springbootproject.payload.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.Getter;
import org.example.springbootproject.utils.Constants;
import org.example.springbootproject.validation.ContractNameUnique;
import org.example.springbootproject.validation.ValidateDateRange;
import java.util.Set;

@Data
@Getter
@ValidateDateRange(message = "{common.date_range}")
public class CreateContractRequest {

    @NotBlank(message = "{common.required}")
    @ContractNameUnique(message = "{contract.name.duplicated}")
    private String contractName;

    @NotBlank(message = "{common.required}")
    private String startDate;

    @NotBlank(message = "{common.required}")
    private String endDate;

    @Min(value = Constants.MIN_SIZE_NUMBER, message = "{common.number_min}")
    @Max(value = Constants.MAX_SIZE_NUMBER, message = "{common.number_max}")
    private float deposit;

    private int type;

    private int roomId;

    private Set<Integer> tenants;
    private String language;
}


