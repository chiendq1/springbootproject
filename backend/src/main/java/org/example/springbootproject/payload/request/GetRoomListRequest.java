package org.example.springbootproject.payload.request;

import lombok.Data;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Data
@Getter
public class GetRoomListRequest {
    private Integer pageNo = 0;
    private String sortBy = "id";
    private String searchValue = "";
    private float[] priceRange = {0, 0};
    private int[] capacity = {0, 0};
    private float[] area = {0, 0};
    private int[] status;
}
