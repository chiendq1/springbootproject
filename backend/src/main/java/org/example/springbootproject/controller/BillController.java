package org.example.springbootproject.controller;

import jakarta.validation.Valid;
import org.example.springbootproject.entity.Bill;
import org.example.springbootproject.payload.request.CreateBillRequest;
import org.example.springbootproject.payload.request.GenerateBillPdfRequest;
import org.example.springbootproject.payload.request.GetBillListRequest;
import org.example.springbootproject.payload.request.UpdateBillRequest;
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
@RequestMapping("/api/bills")
public class BillController extends BaseController {

    @Autowired
    private AuthService authService;

    @Autowired
    private BillService billService;

    @Autowired
    private FileService fileService;

    @Autowired
    private EmailService emailService;

    @Autowired
    private CurrencyService currencyService;

    @GetMapping()
    public ResponseEntity<ApiResponse<Map<String, Object>>> index(
            @RequestParam(value = "month", required = false) String month,
            @RequestParam(value = "roomId", required = false) Integer roomId,
            @RequestParam(value = "searchValue", required = false, defaultValue = "") String searchValue,
            @RequestParam(value = "pageNo", required = false, defaultValue = "0") int pageNo,
            @RequestParam(value = "status", required = false) Integer status
    ) {
        try {
            GetBillListRequest request = new GetBillListRequest();

            Date dateMonth = (month != null && !month.isEmpty()) ? Date.valueOf(month) : null;

            request.setMonth(dateMonth);
            request.setRoomId(roomId);
            request.setSearchValue(searchValue);
            request.setStatus(status);
            request.setPageNo(pageNo);

            String currentUserName = authService.getCurrentUser().getUsername();
            Map<String, Object> response = billService.getListBills(request, currentUserName);
            return new ResponseEntity<>(new ApiResponse<>(true, "get bills success", response, HttpStatus.OK), HttpStatus.OK);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return new ResponseEntity<>(new ApiResponse<>(false, "get bills failed", null, HttpStatus.BAD_REQUEST), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/create")
    @PreAuthorize("@roomService.hasRoom(#request.roomId, authentication.name, true) or hasRole('ADMIN')")
    public ResponseEntity<?> create(@RequestBody @Valid CreateBillRequest request) {
        try {
            String language = request.getLanguage().isEmpty() ? Constants.EN_LANGUAGE : request.getLanguage();
            String templateName = "bill_template_" + language;
            float exchangeRate = currencyService.getCurrentExchangeRate();
            Map<String, Object> bill = billService.createBill(request);
            if (bill == null) {
                return new ResponseEntity<>(new ApiResponse<>(false, "create bill failed", null, HttpStatus.BAD_REQUEST), HttpStatus.BAD_REQUEST);
            }

            Map<String, Object> billPdfData = billService.getBillPdfData((Bill) bill.get("bill"), exchangeRate);
            String htmlContent = fileService.parseThymeleafTemplate("bill_details_" + language, billPdfData);
            ByteArrayOutputStream outputStream = fileService.generatePdfFromHtml(htmlContent);
            emailService.sendEmailBill(templateName, billPdfData, outputStream);

            return new ResponseEntity<>(new ApiResponse<>(true, "create bill success", bill, HttpStatus.OK), HttpStatus.OK);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return new ResponseEntity<>(new ApiResponse<>(false, "create bill failed", null, HttpStatus.BAD_REQUEST), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/{id}")
    @PreAuthorize("@billService.checkBillRelatedTo(#id, authentication.name, true) or hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Map<String, Object>>> show(@PathVariable int id) {
        Map<String, Object> bill = billService.getBillDetails(id, true);

        if (bill == null) {
            return new ResponseEntity<>(new ApiResponse<>(false, "get bill details failed", null, HttpStatus.BAD_REQUEST), HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(new ApiResponse<>(true, "get bill details success", bill, HttpStatus.OK), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    @PreAuthorize("@billService.checkBillRelatedTo(#id, authentication.name, true) or hasRole('ADMIN')")
    ResponseEntity<ApiResponse<Map<String, Object>>> update(@PathVariable int id, @RequestBody UpdateBillRequest request) {
        Map<String, Object> bill = billService.updateBill(id, request.getStatus());

        if (bill == null) {
            return new ResponseEntity<>(new ApiResponse<>(false, "update bill details failed", null, HttpStatus.BAD_REQUEST), HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(new ApiResponse<>(true, "update bill details success", bill, HttpStatus.OK), HttpStatus.OK);
    }

    @PostMapping("/pdf")
    public ResponseEntity<?> exportPdf(@RequestBody GenerateBillPdfRequest request) {
        String language = request.getLanguage().isEmpty() ? Constants.EN_LANGUAGE : request.getLanguage();
        String templateName = "bill_details_" + language;
        float exhangeRate = currencyService.getCurrentExchangeRate();
        Map<String, Object> bill = billService.getBillDetails(request.getBillId(), false);
        Map<String, Object> billPdfData = billService.getBillPdfData((Bill) bill.get("bill"), exhangeRate);
        String htmlContent = fileService.parseThymeleafTemplate(templateName, billPdfData);
        ByteArrayOutputStream outputStream = fileService.generatePdfFromHtml(htmlContent);
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=bill_" + "file.pdf");

        return ResponseEntity
                .ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_PDF)
                .body(new InputStreamResource(new ByteArrayInputStream(outputStream.toByteArray())));
    }
}
