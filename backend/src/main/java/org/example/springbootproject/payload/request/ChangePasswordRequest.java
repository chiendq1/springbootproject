package org.example.springbootproject.payload.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
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
public class ChangePasswordRequest {

    @NotBlank(message = "{common.required}")
    @Size(max = Constants.STRING_MAX_LENGTH, message = "{common.string_max_length}")
    private String oldPassword;

    @NotBlank(message = "{common.required}")
    @Size(min = Constants.PASSWORD_MIN_LENGTH, max = Constants.PASSWORD_MAX_LENGTH, message = "{password.between}")
    @Pattern(
            regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]+$",
            message = "{password.invalid}"
    )
    private String newPassword;
}
