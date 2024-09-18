package org.example.springbootproject.payload.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.Getter;
import org.example.springbootproject.utils.Constants;
import org.example.springbootproject.validation.FieldExist;

@Data
@Getter
public class CreateUserRequest {

    @NotBlank(message = "{common.required}")
    @FieldExist(fieldName = "username", duplicate = true, message = "{username.duplicated}")
    @Pattern(regexp = "^[a-zA-Z0-9]+$", message = "{username.invalid}")
    @Size(max = Constants.STRING_MAX_LENGTH, message = "{common.string_max_length}")
    private String username;

    @NotBlank(message = "{common.required}")
    @FieldExist(fieldName = "email", duplicate = true, message = "{email.duplicated}")
    @Pattern(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$", message = "{email.invalid}")
    @Size(max = Constants.STRING_MAX_LENGTH, message = "{common.string_max_length}")
    private String email;

    @NotBlank(message = "{common.required}")
    @Size(max = Constants.STRING_MAX_LENGTH, message = "{common.string_max_length}")
    private String fullName;

    @NotBlank(message = "{common.required}")
    @FieldExist(fieldName = "phoneNumber", duplicate = true, message = "{phone.duplicated}")
    @Size(max = Constants.STRING_MAX_LENGTH, message = "{common.string_max_length}")
    @Pattern(regexp = "^[+]?[0-9]{10,15}$", message = "{phone.invalid}")
    private String phoneNumber;

    @Size(max = Constants.STRING_MAX_LENGTH, message = "{common.string_max_length}")
    private String location;

    @NotBlank(message = "{common.required}")
    @Pattern(regexp = "^(LANDLORD|TENANT)$", message = "{role.invalid}")
    private String highestRole = "TENANT";
}
