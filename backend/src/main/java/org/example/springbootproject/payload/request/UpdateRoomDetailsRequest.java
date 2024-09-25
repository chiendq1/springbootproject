package org.example.springbootproject.payload.request;

import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.Getter;
import org.example.springbootproject.utils.Constants;

import java.util.Set;

@Data
@Getter
public class UpdateRoomDetailsRequest {

    private int roomId;

    @NotBlank(message = "{common.required}")
    @Size(max = Constants.STRING_MAX_LENGTH, message = "{common.string_max_length}")
    private String roomCode;

    @Min(value = Constants.MIN_SIZE_NUMBER, message = "{common.number_min}")
    @Max(value = Constants.MAX_SIZE_NUMBER, message = "{common.number_max}")
    private float area;

    @Min(value = Constants.MIN_SIZE_NUMBER, message = "{common.number_min}")
    @Max(value = Constants.MAX_SIZE_NUMBER, message = "{common.number_max}")
    private int capacity;

    @Min(value = Constants.MIN_SIZE_NUMBER, message = "{common.number_min}")
    @Max(value = Constants.MAX_SIZE_NUMBER, message = "{common.number_max}")
    private int rentPrice;

    @Min(value = 0, message = "{status.invalid}")
    @Max(value = 2, message = "{status.invalid}")
    private int status;

    private Set<Integer> utilities;
}
