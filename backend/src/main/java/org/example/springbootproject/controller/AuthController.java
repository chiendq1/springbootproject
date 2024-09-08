package org.example.springbootproject.controller;

import jakarta.validation.Valid;
import org.example.springbootproject.payload.request.LoginRequest;
import org.example.springbootproject.payload.request.RegisterRequest;
import org.example.springbootproject.payload.response.ApiResponse;
import org.example.springbootproject.service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController extends BaseController {
    private final AuthService authService;
    private final AuthenticationManager authenticationManager;

    public AuthController(AuthService authService, AuthenticationManager authenticationManager) {
        this.authService = authService;
        this.authenticationManager = authenticationManager;
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<String>> login(@RequestBody @Valid LoginRequest loginRequest) {
        try {
            Authentication authenticationResponse = authService.authenticate(loginRequest, authenticationManager);
            String token = authService.generateToken(loginRequest.getUsername());

            // Check if authentication was successful
            if (authenticationResponse.isAuthenticated() && token != null) {
                ApiResponse<String> responseApi = new ApiResponse<>(true, "login success", token, HttpStatus.OK);

                return new ResponseEntity<>(responseApi, HttpStatus.OK);
            }

            return new ResponseEntity<>(new ApiResponse<String>(false, "login fail", "", HttpStatus.FORBIDDEN), HttpStatus.FORBIDDEN);
        } catch (Exception e) {
            // Handle any exceptions that occur during authentication
            logger.error(e.getMessage());

            return new ResponseEntity<>(new ApiResponse<String>(false, "login fail", "", HttpStatus.FORBIDDEN), HttpStatus.FORBIDDEN);
        }
    }


    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody RegisterRequest registerRequest) {
        try {
            authService.registerUser(registerRequest);

            return ResponseEntity.status(HttpStatus.CREATED).body("User registered successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Registration failed: " + e.getMessage());
        }
    }
}
