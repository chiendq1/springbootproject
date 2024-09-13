package org.example.springbootproject.controller;

import org.example.springbootproject.dto.UserDto;
import org.example.springbootproject.payload.request.ResetPasswordRequest;
import org.example.springbootproject.payload.response.ApiResponse;
import org.example.springbootproject.service.AuthService;
import org.example.springbootproject.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController extends BaseController {

    @Autowired
    private UserService userService;

    @Autowired
    private AuthService authService;

    @GetMapping()
    public List<UserDto> index() {
        return userService.getAllDtoUsers();
    }

    @GetMapping( "/{id}")
    @PreAuthorize("@authService.hasId(#id, authentication.name)")
    public ResponseEntity<ApiResponse<UserDto>> show(@PathVariable int id) {
        UserDto userDto = userService.getUserDtoByById(id);
        ApiResponse<UserDto> apiResponse = new ApiResponse<UserDto>();
        apiResponse.setStatus(userDto == null ? HttpStatus.BAD_REQUEST : HttpStatus.OK);
        apiResponse.setData(userDto);
        apiResponse.setMessage(userDto == null ? "get user details failed": "get user details success");
        apiResponse.setSuccess(userDto == null);

        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    @PutMapping( "/{id}")
    @PreAuthorize("@authService.hasId(#id, authentication.name)")
    public ResponseEntity<ApiResponse<UserDto>> update(@PathVariable int id, @RequestBody UserDto userDto) {
        UserDto updatedUser = userService.updateUserDtoById(id, userDto);

        if(updatedUser == null) {
            return new ResponseEntity<>(new ApiResponse<>(false, "get user details failed", null, HttpStatus.BAD_REQUEST), HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(new ApiResponse<>(true, "get user details success", updatedUser, HttpStatus.OK), HttpStatus.OK);
    }

    @PutMapping( "/password/{id}")
    @PreAuthorize("@authService.hasId(#id, authentication.name)")
    public ResponseEntity<ApiResponse<String>> updatePassword(@PathVariable int id, @RequestBody ResetPasswordRequest passwordRequest) {
        if(authService.verifyPassword(id, passwordRequest.getOldPassword()) && userService.resetPassword(id, passwordRequest.getNewPassword())) {
            return new ResponseEntity<>(new ApiResponse<>(true, "reset password success", "", HttpStatus.OK), HttpStatus.OK);
        }

        return new ResponseEntity<>(new ApiResponse<>(false, "reset password failed", "", HttpStatus.BAD_REQUEST), HttpStatus.BAD_REQUEST);
    }
}
