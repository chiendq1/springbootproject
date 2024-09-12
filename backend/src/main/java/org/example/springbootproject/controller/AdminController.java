package org.example.springbootproject.controller;

import org.example.springbootproject.payload.response.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin")
public class AdminController extends BaseController {

    @GetMapping("")
    public ResponseEntity<ApiResponse<String>> index() {
        ApiResponse<String> responseApi = new ApiResponse<>(true, "welcome to admin", "admin api", HttpStatus.OK);

        return new ResponseEntity<>(responseApi, HttpStatus.OK);
    }
}
