package org.example.springbootproject.payload.request;

import lombok.Data;
import lombok.Getter;

import java.util.Set;

@Data
@Getter
public class RoomTenantsRequest {
    private Set<Integer> listTenants;
}
