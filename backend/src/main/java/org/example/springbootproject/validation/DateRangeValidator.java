package org.example.springbootproject.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.extern.slf4j.Slf4j;
import org.example.springbootproject.payload.request.CreateContractRequest;
import org.example.springbootproject.service.ContractService;
import org.springframework.beans.factory.annotation.Autowired;

import java.sql.Date;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Slf4j
public class DateRangeValidator implements ConstraintValidator<ValidateDateRange, CreateContractRequest> {

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @Autowired
    private ContractService contractService;

    @Override
    public void initialize(ValidateDateRange constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(CreateContractRequest request, ConstraintValidatorContext context) {
        try {
            LocalDate currentDate = LocalDate.now();
            LocalDate startDate = LocalDate.parse(request.getStartDate(), formatter);
            LocalDate endDate = LocalDate.parse(request.getEndDate(), formatter);
            if (startDate.isBefore(currentDate)) {
                // Disable default constraint violation message
                context.disableDefaultConstraintViolation();

                // Add a custom violation with a key
                context.buildConstraintViolationWithTemplate("E-CM-022")
                        .addConstraintViolation();
                return false;
            }

            if (contractService.checkContractDateValid(Date.valueOf(request.getStartDate()), request.getRoomId())) {
                // Disable default constraint violation message
                context.disableDefaultConstraintViolation();

                // Add a custom violation with a key
                context.buildConstraintViolationWithTemplate("E-CM-023")
                        .addConstraintViolation();
                return false;
            }

            return endDate.isAfter(startDate);  // Validate that endDate is after startDate
        } catch (Exception e) {
            log.error(e.getMessage());
            return false;  // Return false if dates are invalid or cannot be parsed
        }
    }
}
