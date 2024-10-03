package org.example.springbootproject.controller;

import jakarta.validation.Valid;
import org.example.springbootproject.payload.request.CreateUtilityRequest;
import org.example.springbootproject.payload.request.UpdateUtilityRequest;
import org.example.springbootproject.payload.response.ApiResponse;
import org.example.springbootproject.service.UtilityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/utilities")
public class UtilityController extends BaseController {

    @Autowired
    private UtilityService utilityService;

    @GetMapping()
    public ResponseEntity<ApiResponse<Map<String, Object>>> index(
            @RequestParam(value = "searchValue", required = false) String searchValue,
            @RequestParam(value = "status", required = false) Integer status
    ) {
        try {

            Map<String, Object> response = utilityService.getListUtilities(searchValue, status);
            return new ResponseEntity<>(new ApiResponse<>(true, "get list utilities success", response, HttpStatus.OK), HttpStatus.OK);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return new ResponseEntity<>(new ApiResponse<>(false, "get list utilities failed", null, HttpStatus.BAD_REQUEST), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Map<String, Object>>> index(@PathVariable int id) {
        try {
            Map<String, Object> response = utilityService.getUtilityDetails(id);
            return new ResponseEntity<>(new ApiResponse<>(true, "get utility details success", response, HttpStatus.OK), HttpStatus.OK);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return new ResponseEntity<>(new ApiResponse<>(false, "get utility details failed", null, HttpStatus.BAD_REQUEST), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Map<String, Object>>> create(@RequestBody @Valid CreateUtilityRequest request) {
        try {
            Map<String, Object> response = utilityService.createNewUtility(request);
            return new ResponseEntity<>(new ApiResponse<>(true, "create utility success", response, HttpStatus.OK), HttpStatus.OK);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return new ResponseEntity<>(new ApiResponse<>(false, "create utility failed", null, HttpStatus.BAD_REQUEST), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> update(@PathVariable int id, @RequestBody @Valid UpdateUtilityRequest request) {
        try {
            Map<String, Object> errors = new HashMap<>();
            if (utilityService.checkDuplicateName(request.getName(), id)) {
                errors.put("name", "E-CM-026");
                return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
            }

            Map<String, Object> response = utilityService.updateUtility(request, id);
            return new ResponseEntity<>(new ApiResponse<>(true, "create utility success", response, HttpStatus.OK), HttpStatus.OK);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return new ResponseEntity<>(new ApiResponse<>(false, "create utility failed", null, HttpStatus.BAD_REQUEST), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/deactivate/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deactivate(@PathVariable int id) {
        try {
            Map<String, Object> response = utilityService.deactivateUtility(id);
            if (response == null) {
                return new ResponseEntity<>(new ApiResponse<>(false, "create utility failed", null, HttpStatus.BAD_REQUEST), HttpStatus.BAD_REQUEST);
            }
            return new ResponseEntity<>(new ApiResponse<>(true, "create utility success", response, HttpStatus.OK), HttpStatus.OK);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return new ResponseEntity<>(new ApiResponse<>(false, "create utility failed", null, HttpStatus.BAD_REQUEST), HttpStatus.BAD_REQUEST);
        }
    }
}
