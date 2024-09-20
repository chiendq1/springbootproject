package org.example.springbootproject.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {

    private int id;
    private String username;
    private String email;
    private String fullName;
    private String phoneNumber;
    private String location;
    private Set<RoleDto> roles;
    private String highestRole;
}
