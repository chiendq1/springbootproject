package org.example.springbootproject.payload.request;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import java.sql.Date;

@Data
@Getter
@Setter
public class GetBillListRequest {
    private Date month;
    private Integer roomId;
    private String sortBy = "createAt";
    private String searchValue;
    private Integer pageNo;
    private Integer status;
}
