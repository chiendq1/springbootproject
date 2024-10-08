package org.example.springbootproject.payload.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.springbootproject.utils.Constants;
import org.example.springbootproject.validation.FieldExist;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Getter
public class ResetPasswordRequest {

    @NotBlank(message = "{common.required}")
    @FieldExist(fieldName = "email", message = "{email.not_exist}")
    @Size(max = Constants.STRING_MAX_LENGTH, message = "{common.string_max_length}")
    private String email;

    @NotBlank(message = "{common.required}")
    @Size(max = Constants.STRING_MAX_LENGTH, message = "{common.string_max_length}")
    private String otpCode;

    @NotBlank(message = "{common.required}")
    @Size(min = Constants.PASSWORD_MIN_LENGTH, max = Constants.PASSWORD_MAX_LENGTH, message = "{password.between}")
    @Pattern(
            regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]+$",
            message = "{password.invalid}"
    )
    private String newPassword;
}
