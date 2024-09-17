package org.example.springbootproject.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.example.springbootproject.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;

public class FieldExistValidator implements ConstraintValidator<FieldExist, String> {

    @Autowired
    private UserService userService;

    private String fieldName;

    @Override
    public void initialize(FieldExist constraintAnnotation) {
        this.fieldName = constraintAnnotation.fieldName();
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        return userService.checkFieldExisted(fieldName, s);
    }
}
