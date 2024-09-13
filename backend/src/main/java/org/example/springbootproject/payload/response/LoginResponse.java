package org.example.springbootproject.payload.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.example.springbootproject.dto.UserDto;

@AllArgsConstructor
@Getter
@Setter
public class LoginResponse {
    private String token;
    private String refreshToken;
    private UserDto user;
}
