package org.example.springbootproject.payload.request;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Component
@Getter
@Setter
public class CreateBillDetailsRequest {
    private Integer utilityId;
    private int amount;
}
