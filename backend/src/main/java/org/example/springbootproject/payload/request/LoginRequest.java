package org.example.springbootproject.payload.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.Getter;
import org.example.springbootproject.utils.Constants;

@Data
@Getter
public class LoginRequest {

    @NotBlank(message = "{common.required}")
    @Size(max = Constants.STRING_MAX_LENGTH, message = "{common.string_max_length}")
    private String username;

    @NotBlank(message = "{common.required}")
    @Size(max = Constants.STRING_MAX_LENGTH, message = "{common.string_max_length}")
    private String password;
}
