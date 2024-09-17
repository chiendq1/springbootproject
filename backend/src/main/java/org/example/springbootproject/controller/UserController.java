package org.example.springbootproject.controller;

import jakarta.validation.Valid;
import org.example.springbootproject.dto.UserDto;
import org.example.springbootproject.entity.User;
import org.example.springbootproject.payload.request.ChangePasswordRequest;
import org.example.springbootproject.payload.request.ProfileUpdateRequest;
import org.example.springbootproject.payload.response.ApiResponse;
import org.example.springbootproject.service.AuthService;
import org.example.springbootproject.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
public class UserController extends BaseController {

    @Autowired
    private UserService userService;

    @Autowired
    private AuthService authService;

    @GetMapping()
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<List<UserDto>>> index(
            @RequestParam(defaultValue = "0") Integer pageNo,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(defaultValue = "id") String sortBy) {
//        List<UserDto> list = userService.listUsersByRole(pageNo, pageSize, sortBy);
        return new ResponseEntity<>(new ApiResponse<>(false, "get users failed", null, HttpStatus.BAD_REQUEST), HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/{id}")
    @PreAuthorize("@userService.hasId(#id, authentication.name)")
    public ResponseEntity<ApiResponse<UserDto>> show(@PathVariable int id) {
        UserDto userDto = userService.getUserDtoByById(id);
        ApiResponse<UserDto> apiResponse = new ApiResponse<UserDto>();
        apiResponse.setStatus(userDto == null ? HttpStatus.BAD_REQUEST : HttpStatus.OK);
        apiResponse.setData(userDto);
        apiResponse.setMessage(userDto == null ? "get user details failed" : "get user details success");
        apiResponse.setSuccess(userDto == null);

        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    @PreAuthorize("@userService.hasId(#id, authentication.name)")
    public ResponseEntity<?> update(@PathVariable int id, @RequestBody @Valid ProfileUpdateRequest request) {
        String emailErrorCode = userService.checkFieldDuplicated("email", request.getEmail(), id);
        String phoneErrorCode = userService.checkFieldDuplicated("phoneNumber", request.getPhoneNumber(), id);
        Map<String, String> errors = new HashMap<>();

        if (!emailErrorCode.isEmpty() || !phoneErrorCode.isEmpty()) {
            errors.put("email", emailErrorCode);
            errors.put("phoneNumber", phoneErrorCode);
            return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
        }

        UserDto updatedUser = userService.updateUserDtoById(id, request);

        if (updatedUser == null) {
            return new ResponseEntity<>(new ApiResponse<>(false, "get user details failed", null, HttpStatus.BAD_REQUEST), HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(new ApiResponse<>(true, "get user details success", updatedUser, HttpStatus.OK), HttpStatus.OK);
    }

    @PutMapping("/password/{id}")
    @PreAuthorize("@userService.hasId(#id, authentication.name)")
    public ResponseEntity<ApiResponse<String>> updatePassword(@PathVariable int id, @RequestBody @Valid ChangePasswordRequest passwordRequest) {
        if (authService.verifyPassword(id, passwordRequest.getOldPassword()) && userService.resetPassword(id, passwordRequest.getNewPassword())) {
            return new ResponseEntity<>(new ApiResponse<>(true, "reset password success", "", HttpStatus.OK), HttpStatus.OK);
        }

        return new ResponseEntity<>(new ApiResponse<>(false, "reset password failed", "", HttpStatus.BAD_REQUEST), HttpStatus.BAD_REQUEST);
    }
}
