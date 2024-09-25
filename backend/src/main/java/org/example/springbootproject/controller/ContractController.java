package org.example.springbootproject.controller;

import org.example.springbootproject.payload.request.GetContractListRequest;
import org.example.springbootproject.payload.response.ApiResponse;
import org.example.springbootproject.service.AuthService;
import org.example.springbootproject.service.ContractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Date;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/contracts")
public class ContractController extends BaseController {

    @Autowired
    private AuthService authService;

    @Autowired
    private ContractService contractService;

    @GetMapping()
    public ResponseEntity<ApiResponse<Map<String, Object>>> index(
            @RequestParam(value = "startDate", required = false) String startDate,
            @RequestParam(value = "endDate", required = false) String endDate,
            @RequestParam(value = "roomId", required = false) Integer roomId,
            @RequestParam(value = "searchValue", required = false, defaultValue = "") String searchValue,
            @RequestParam(value = "tenantId", required = false) Integer tenantId,
            @RequestParam(value = "landlordId", required = false) Integer landlordId,
            @RequestParam(value = "pageNo", required = false, defaultValue = "0") int pageNo,
            @RequestParam(value = "status", required = false) Integer status,
            @RequestParam(value = "type", required = false) Integer type
    ) {
        try {
            GetContractListRequest request = new GetContractListRequest();

            Date start = (startDate != null && !startDate.isEmpty()) ? Date.valueOf(startDate) : null;
            Date end = (endDate != null && !endDate.isEmpty()) ? Date.valueOf(endDate) : null;

            request.setStartDate(start);
            request.setEndDate(end);
            request.setRoomId(roomId);
            request.setSearchValue(searchValue);
            request.setTenantId(tenantId);
            request.setLandlordId(landlordId);
            request.setType(type);
            request.setStatus(status);
            request.setPageNo(pageNo);

            String currentUserName = authService.getCurrentUser().getUsername();
            Map<String, Object> response = contractService.getListContracts(request, currentUserName);
            return new ResponseEntity<>(new ApiResponse<>(true, "get contracts success", response, HttpStatus.OK), HttpStatus.OK);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return new ResponseEntity<>(new ApiResponse<>(false, "get contracts failed", null, HttpStatus.BAD_REQUEST), HttpStatus.BAD_REQUEST);
        }
    }
}
