package org.example.springbootproject.payload.response;

import lombok.Data;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.springframework.http.HttpStatus;

import java.io.Serializable;

@Data
@JsonPropertyOrder({
        "success",
        "message",
        "data"
})
public class ApiResponse<T> implements Serializable {

    @JsonIgnore
    private static final long serialVersionUID = 7702134516418120340L;

    @JsonProperty("success")
    private Boolean success;

    @JsonProperty("data")
    private T data;

    @JsonProperty("message")
    private String message;

    @JsonIgnore
    private HttpStatus status;

    public ApiResponse() {

    }

    public ApiResponse(Boolean success, String message, T data, HttpStatus httpStatus) {
        this.success = success;
        this.message = message;
        this.data = data;
        this.status = httpStatus;
    }
}