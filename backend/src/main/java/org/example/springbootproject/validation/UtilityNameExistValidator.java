package org.example.springbootproject.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.example.springbootproject.repository.UtilityRepository;
import org.example.springbootproject.utils.Constants;
import org.springframework.beans.factory.annotation.Autowired;

public class UtilityNameExistValidator implements ConstraintValidator<UtilityNameExist, String> {

    @Autowired
    private UtilityRepository utilityRepository;

    @Override
    public void initialize(UtilityNameExist constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        return !utilityRepository.existsUtilityByEnNameOrJaNameAndStatusNot(s.trim(), s.trim(), Constants.ROOM_UTILITY_STATUS_INACTIVE);
    }
}
