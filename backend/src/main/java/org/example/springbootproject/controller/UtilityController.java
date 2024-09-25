package org.example.springbootproject.controller;

import org.example.springbootproject.payload.response.ApiResponse;
import org.example.springbootproject.service.UtilityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/utility")
public class UtilityController extends BaseController {

    @Autowired
    private UtilityService utilityService;

    @GetMapping()
    public ResponseEntity<ApiResponse<Map<String, Object>>> index() {
        try {

            Map<String, Object> response = utilityService.getListUtilities();
            return new ResponseEntity<>(new ApiResponse<>(true, "get list utilities success", response, HttpStatus.OK), HttpStatus.OK);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return new ResponseEntity<>(new ApiResponse<>(false, "get list utilities failed", null, HttpStatus.BAD_REQUEST), HttpStatus.BAD_REQUEST);
        }
    }
}
