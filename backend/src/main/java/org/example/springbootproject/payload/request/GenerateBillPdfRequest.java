package org.example.springbootproject.payload.request;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class GenerateBillPdfRequest {
    private int billId;
    private String language;
}
