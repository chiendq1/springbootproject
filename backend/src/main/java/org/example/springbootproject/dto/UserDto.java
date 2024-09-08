package org.example.springbootproject.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;

@Getter
public class UserDto {

    @Id
    private int id;

    @Setter
    @Size(max = 50)
    private String username;

    @NotBlank()
    @Size(min = 8, max = 20)
    private String password;

    @Setter
    @NotBlank()
    @Email()
    @Size(max = 100)
    private String email;

    @Setter
    @NotBlank()
    private String role;
}
