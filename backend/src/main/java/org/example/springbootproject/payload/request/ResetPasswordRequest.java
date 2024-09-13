package org.example.springbootproject.payload.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.springbootproject.utils.Constants;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Getter
public class ResetPasswordRequest {

    @NotBlank(message = "{common.required}")
    @Size(max = Constants.STRING_MAX_LENGTH, message = "{common.string_max_length}")
    private String oldPassword;

    @NotBlank(message = "{common.required}")
    @
    @Size(max = Constants.STRING_MAX_LENGTH, message = "{common.string_max_length}")
    private String newPassword;
}
