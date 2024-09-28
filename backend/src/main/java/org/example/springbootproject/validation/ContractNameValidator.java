package org.example.springbootproject.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.example.springbootproject.service.ContractService;
import org.springframework.beans.factory.annotation.Autowired;

public class ContractNameValidator implements ConstraintValidator<ContractNameUnique, String> {

    @Autowired
    private ContractService contractService;

    @Override
    public void initialize(ContractNameUnique constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        return !contractService.checkContractExistByField("contractName", s.trim());
    }
}
