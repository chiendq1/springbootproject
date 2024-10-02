package org.example.springbootproject.payload.request;

import lombok.Getter;
import lombok.Setter;
import org.example.springbootproject.validation.BillCodeExist;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Getter
@Setter
public class CreateBillRequest {

    @BillCodeExist(message = "{bill.code_exist}")
    private String billCode;
    private Integer roomId;
    private List<CreateBillDetailsRequest> details;
    private String language;
}
