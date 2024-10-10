package org.example.springbootproject.controller;

import jakarta.validation.Valid;
import org.example.springbootproject.dto.ContractDto;
import org.example.springbootproject.payload.request.CreateContractRequest;
import org.example.springbootproject.payload.request.GenerateContractPdfRequest;
import org.example.springbootproject.payload.request.GetContractListRequest;
import org.example.springbootproject.payload.response.ApiResponse;
import org.example.springbootproject.service.*;
import org.example.springbootproject.utils.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.sql.Date;
import java.util.Map;

@RestController
@RequestMapping("/api/contracts")
public class ContractController extends BaseController {

    @Autowired
    private AuthService authService;

    @Autowired
    private ContractService contractService;

    @Autowired
    private CurrencyService currencyService;

    @Autowired
    private FileService fileService;

    @Autowired
    private EmailService emailService;

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

    @PostMapping()
    @PreAuthorize("@roomService.hasRoom(#request.roomId, authentication.name, true) or hasRole('ADMIN')")
    public ResponseEntity<?> create(@RequestBody @Valid CreateContractRequest request) {
        try {
            float exchangeRate = currencyService.getCurrentExchangeRate();
            Map<String, Object> contractData = contractService.createContract(request, exchangeRate);
            if(contractData == null) {
                return new ResponseEntity<>(new ApiResponse<>(false, "create contract failed", null, HttpStatus.BAD_REQUEST), HttpStatus.BAD_REQUEST);
            }

            if(contractData.get("tenants") != null) {
                return new ResponseEntity<>(contractData, HttpStatus.BAD_REQUEST);
            }

            String language = request.getLanguage().isEmpty() ? Constants.EN_LANGUAGE : request.getLanguage();
            String templateName = "contract_template_" + language;
            ContractDto contractDto = (ContractDto) contractData.get("contract");
            Map<String, Object> contractDataPdf = contractService.getContractData(contractDto.getId(), exchangeRate);
            String htmlContent = fileService.parseThymeleafTemplate(templateName, contractDataPdf);
            ByteArrayOutputStream outputStream = fileService.generatePdfFromHtml(htmlContent);
            emailService.sendEmailCreateContract("contract_create_" + language, contractDataPdf, outputStream);

            return new ResponseEntity<>(new ApiResponse<>(true, "create contract success", contractData, HttpStatus.OK), HttpStatus.OK);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);

            return new ResponseEntity<>(new ApiResponse<>(false, "create contract failed", null, HttpStatus.BAD_REQUEST), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/{id}")
    @PreAuthorize("@contractService.checkContractRelatedTo(#id, authentication.name, true) or hasRole('ADMIN')")
    public ResponseEntity<?> show(@PathVariable int id) {
        try {
            Map<String, Object> response = contractService.getContractDetails(id);

            return new ResponseEntity<>(new ApiResponse<>(true, "get contract details success", response, HttpStatus.OK), HttpStatus.OK);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);

            return new ResponseEntity<>(new ApiResponse<>(false, "get contract details failed", null, HttpStatus.BAD_REQUEST), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/{id}")
    @PreAuthorize("@contractService.checkContractRelatedTo(#id, authentication.name, false) or hasRole('ADMIN')")
    public ResponseEntity<?> terminateContract(@PathVariable int id) {
        try {
            Map<String, Object> response = contractService.terminateContract(id);

            return new ResponseEntity<>(new ApiResponse<>(true, "terminate contract success", response, HttpStatus.OK), HttpStatus.OK);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);

            return new ResponseEntity<>(new ApiResponse<>(false, "terminate contract failed", null, HttpStatus.BAD_REQUEST), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/pdf")
    public ResponseEntity<?> exportPdf(@RequestBody GenerateContractPdfRequest request) {
        String language = request.getLanguage().isEmpty() ? Constants.EN_LANGUAGE : request.getLanguage();
        String templateName = "contract_template_" + language;
        float exchangeRate = currencyService.getCurrentExchangeRate();
        Map<String, Object> contractData = contractService.getContractData(request.getContractId(), exchangeRate);
        String htmlContent = fileService.parseThymeleafTemplate(templateName, contractData);
        ByteArrayOutputStream outputStream = fileService.generatePdfFromHtml(htmlContent);
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=contract_" + request.getContractId() + ".pdf");

        return ResponseEntity
                .ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_PDF)
                .body(new InputStreamResource(new ByteArrayInputStream(outputStream.toByteArray())));
    }
}
