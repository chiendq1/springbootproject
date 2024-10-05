package org.example.springbootproject.controller;

import org.example.springbootproject.payload.response.ApiResponse;
import org.example.springbootproject.service.CurrencyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/api/currencies")
public class CurrencyController extends BaseController {

    @Autowired
    private CurrencyService currencyService;

    @GetMapping()
    public ResponseEntity<ApiResponse<Map<String, Object>>> index(
            @RequestParam(value = "language", required = false) String language
    ) throws IOException {
        Map<String, Object> response = currencyService.getExchangeRate(language);
        return new ResponseEntity<>(new ApiResponse<>(true, "get contracts success", response, HttpStatus.OK), HttpStatus.OK);
    }
}
