package org.example.springbootproject.payload.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.Getter;
import org.example.springbootproject.utils.Constants;
import org.example.springbootproject.validation.FieldExist;

@Data
@Getter
public class ForgotPasswordRequest {

    @NotBlank(message = "{common.required}")
    @Size(max = Constants.STRING_MAX_LENGTH, message = "{common.string_max_length}")
    @FieldExist(fieldName = "email", message = "{email.not_exist}")
    private String email;
    private String type;
    private String subject;
}
