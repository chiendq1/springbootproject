package org.example.springbootproject.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.example.springbootproject.dto.UserDto;
import org.example.springbootproject.service.AuthService;
import org.example.springbootproject.service.BillService;
import org.example.springbootproject.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;

public class BillCodeExistValidator implements ConstraintValidator<BillCodeExist, String> {

    @Autowired
    private BillService billService;

    @Autowired
    private AuthService authService;

    @Autowired
    private UserService userService;

    @Override
    public void initialize(BillCodeExist constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        String username = "";
        UserDetails user = authService.getCurrentUser();
        UserDto userDto =  userService.getUserDtoByUsername(user.getUsername());
        if(!userDto.getHighestRole().equals("ADMIN")) {
            username = userDto.getUsername();
        }
        return !billService.checkBillCodeExist(s, username);
    }
}

