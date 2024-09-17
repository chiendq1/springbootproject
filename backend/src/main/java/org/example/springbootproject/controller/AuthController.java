package org.example.springbootproject.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.example.springbootproject.dto.UserDto;
import org.example.springbootproject.payload.request.ForgotPasswordRequest;
import org.example.springbootproject.payload.request.LoginRequest;
import org.example.springbootproject.payload.request.ResetPasswordRequest;
import org.example.springbootproject.payload.request.TokenRefreshRequest;
import org.example.springbootproject.payload.response.ApiResponse;
import org.example.springbootproject.payload.response.LoginResponse;
import org.example.springbootproject.service.AuthService;
import org.example.springbootproject.service.EmailService;
import org.example.springbootproject.service.RefreshTokenService;
import org.example.springbootproject.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController extends BaseController {
    private final AuthService authService;
    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final RefreshTokenService refreshTokenService;
    private final EmailService emailService;

    public AuthController(AuthService authService,
                          AuthenticationManager authenticationManager,
                          RefreshTokenService refreshTokenService,
                          UserService userService,
                          EmailService emailService
    ) {
        this.authService = authService;
        this.authenticationManager = authenticationManager;
        this.refreshTokenService = refreshTokenService;
        this.userService = userService;
        this.emailService = emailService;
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResponse>> login(@RequestBody @Valid LoginRequest loginRequest) {
        try {
            Authentication authenticationResponse = authService.authenticate(loginRequest, authenticationManager);
            String token = authService.generateToken(loginRequest.getUsername(), false);
            if (authenticationResponse.isAuthenticated() && token != null) {
                UserDto user = userService.getUserDtoByUsername(loginRequest.getUsername());
                String refreshToken = authService.generateToken(loginRequest.getUsername(), true);
                LoginResponse loginResponse = new LoginResponse(token, refreshToken, user);
                refreshTokenService.saveRefreshToken(refreshToken, loginRequest.getUsername());

                return new ResponseEntity<>(new ApiResponse<>(true, "login success", loginResponse, HttpStatus.OK), HttpStatus.OK);
            }

            return new ResponseEntity<>(new ApiResponse<>(false, "login fail", null, HttpStatus.FORBIDDEN), HttpStatus.FORBIDDEN);
        } catch (Exception e) {
            // Handle any exceptions that occur during authentication
            logger.error(e.getMessage());

            return new ResponseEntity<>(new ApiResponse<>(false, "login fail", null, HttpStatus.FORBIDDEN), HttpStatus.FORBIDDEN);
        }
    }

    @PostMapping("/refreshtoken")
    public ResponseEntity<?> refreshtoken(@Valid @RequestBody TokenRefreshRequest request) {
        try {
            String requestRefreshToken = request.getRefreshToken();
            String accessToken = refreshTokenService.generateNewAccessToken(requestRefreshToken);
            LoginResponse loginResponse = new LoginResponse(accessToken, requestRefreshToken, null);

            return new ResponseEntity<>(new ApiResponse<>(true, "", loginResponse, HttpStatus.OK), HttpStatus.OK);
        } catch (RuntimeException e) {
            // Handle custom TokenRefreshException
            logger.error(e.getMessage());
            return new ResponseEntity<>(new ApiResponse<>(false, e.getMessage(), null, HttpStatus.FORBIDDEN), HttpStatus.FORBIDDEN);
        } catch (Exception e) {
            // Handle other unexpected exceptions
            logger.error(e.getMessage());
            return new ResponseEntity<>(new ApiResponse<>(false, "An error occurred while refreshing the token", null, HttpStatus.INTERNAL_SERVER_ERROR), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/get-otp")
    public ResponseEntity<?> getOtpCode(@RequestBody @Valid ForgotPasswordRequest request) {
        try {
            emailService.sendResetPasswordEmail(request.getType(), request.getEmail(), request.getSubject());

            return new ResponseEntity<>(new ApiResponse<>(true, "send message success", null, HttpStatus.OK), HttpStatus.OK);
        } catch (Exception e) {
            logger.error(e.getMessage());

            return new ResponseEntity<>(new ApiResponse<>(false, "send message failed", null, HttpStatus.BAD_REQUEST), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody @Valid ResetPasswordRequest resetPasswordRequest) {
        try {
            String otpCode = emailService.getOtpFromCache(resetPasswordRequest.getEmail());
            boolean result = authService.resetPassword(resetPasswordRequest, otpCode);
            if(result) {
                return new ResponseEntity<>(new ApiResponse<>(true, "reset password success", null, HttpStatus.OK), HttpStatus.OK);
            }

            return new ResponseEntity<>(new ApiResponse<>(false, "reset password failed", null, HttpStatus.BAD_REQUEST), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            logger.error(e.getMessage());

            return new ResponseEntity<>(new ApiResponse<>(false, "reset password failed", null, HttpStatus.BAD_REQUEST), HttpStatus.BAD_REQUEST);
        }
    }
}
