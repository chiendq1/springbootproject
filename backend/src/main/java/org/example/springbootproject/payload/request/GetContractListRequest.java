package org.example.springbootproject.payload.request;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.sql.Date;

@Data
@Getter
@Setter
public class GetContractListRequest {
    private Date startDate;
    private Date endDate;
    private String sortBy = "id";
    private Integer roomId;
    private String searchValue;
    private Integer tenantId;
    private Integer landlordId;
    private Integer type;
    private int pageNo;
    private Integer status;
}
