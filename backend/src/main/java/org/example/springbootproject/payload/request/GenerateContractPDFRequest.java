package org.example.springbootproject.payload.request;

import lombok.Getter;
import org.springframework.stereotype.Component;

@Component
@Getter
public class GenerateContractPdfRequest {
    private int contractId;
    private String language;
}
