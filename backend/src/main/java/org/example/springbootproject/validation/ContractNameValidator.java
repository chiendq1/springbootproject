package org.example.springbootproject.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.example.springbootproject.dto.UserDto;
import org.example.springbootproject.entity.User;
import org.example.springbootproject.service.AuthService;
import org.example.springbootproject.service.ContractService;
import org.example.springbootproject.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;

public class ContractNameValidator implements ConstraintValidator<ContractNameUnique, String> {

    @Autowired
    private ContractService contractService;

    @Autowired
    private AuthService authService;

    @Autowired
    private UserService userService;

    @Override
    public void initialize(ContractNameUnique constraintAnnotation) {
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

        return !contractService.checkContractExistByField("contractName", s.trim(), username);
    }
}
