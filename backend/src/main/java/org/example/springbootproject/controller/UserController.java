package org.example.springbootproject.controller;

import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import org.example.springbootproject.dto.UserDto;
import org.example.springbootproject.entity.User;
import org.example.springbootproject.payload.request.ChangePasswordRequest;
import org.example.springbootproject.payload.request.CreateUserRequest;
import org.example.springbootproject.payload.request.ProfileUpdateRequest;
import org.example.springbootproject.payload.response.ApiResponse;
import org.example.springbootproject.service.AuthService;
import org.example.springbootproject.service.EmailService;
import org.example.springbootproject.service.UserService;
import org.example.springbootproject.utils.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UserDetails;
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

    @Autowired
    private EmailService emailService;

    @GetMapping()
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Map<String, Object>>> index(
            @RequestParam(defaultValue = "0") Integer pageNo,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "") String searchValue,
            @RequestParam(defaultValue = "") String filterValue
    ) {
        Map<String, Object> response = userService.listUsersByRole(pageNo, Constants.PAGE_SIZE, sortBy, searchValue.trim(), filterValue.trim());

        return new ResponseEntity<>(new ApiResponse<>(true, "get users success", response, HttpStatus.OK), HttpStatus.OK);
    }

    @PostMapping()
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<UserDto>> create(@RequestBody @Valid CreateUserRequest request) {
        try {
            String password = emailService.generatePassword();
            UserDto userDto = userService.createNewUser(request, password);
            emailService.sendCreateAccountEmail("create_user_email", request.getEmail(), "Create user email", password);

            return new ResponseEntity<>(new ApiResponse<>(true, "create user success", userDto, HttpStatus.OK), HttpStatus.OK);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return new ResponseEntity<>(new ApiResponse<>(false, "create user failed", null, HttpStatus.INTERNAL_SERVER_ERROR), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{id}")
    @PreAuthorize("@userService.hasId(#id, authentication.name) or hasRole('ADMIN')")
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
    @PreAuthorize("@userService.hasId(#id, authentication.name) or hasRole('ADMIN')")
    public ResponseEntity<?> update(@PathVariable int id, @RequestBody @Valid ProfileUpdateRequest request) {
        String emailErrorCode = userService.checkFieldDuplicated("email", request.getEmail(), id);
        String phoneErrorCode = userService.checkFieldDuplicated("phoneNumber", request.getPhoneNumber(), id);
        String userNameErrorCode = userService.checkFieldDuplicated("username", request.getUsername(), id);
        String currentRole = String.valueOf(authService.getCurrentUser().getAuthorities().iterator().next());
        Map<String, String> errors = new HashMap<>();

        if (!emailErrorCode.isEmpty() || !phoneErrorCode.isEmpty() || !userNameErrorCode.isEmpty()) {
            errors.put("email", emailErrorCode);
            errors.put("phoneNumber", phoneErrorCode);
            errors.put("username", userNameErrorCode);
            return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
        }

        UserDto updatedUser = userService.updateUserDtoById(id, request, currentRole);

        if (updatedUser == null) {
            return new ResponseEntity<>(new ApiResponse<>(false, "update user details failed", null, HttpStatus.BAD_REQUEST), HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(new ApiResponse<>(true, "update user details success", updatedUser, HttpStatus.OK), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> delete(@PathVariable int id) {
        try {
            userService.deleteUser(id);

            return new ResponseEntity<>(new ApiResponse<>(true, "delete success", null, HttpStatus.OK), HttpStatus.OK);
        } catch (Exception ex) {
            logger.error(ex.getMessage());
            return new ResponseEntity<>(new ApiResponse<>(false, "delete failed", null, HttpStatus.BAD_REQUEST), HttpStatus.BAD_REQUEST);
        }
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
