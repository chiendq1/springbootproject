package org.example.springbootproject.payload.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.*;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateBillRequest {

    @Min(value = 0, message = "{status.invalid}")
    @Max(value = 2, message = "{status.invalid}")
    private int status;
}
