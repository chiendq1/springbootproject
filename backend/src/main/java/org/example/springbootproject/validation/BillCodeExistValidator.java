package org.example.springbootproject.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.example.springbootproject.service.BillService;
import org.springframework.beans.factory.annotation.Autowired;

public class BillCodeExistValidator implements ConstraintValidator<BillCodeExist, String> {

    @Autowired
    private BillService billService;

    @Override
    public void initialize(BillCodeExist constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        return !billService.checkBillCodeExist(s);
    }
}

