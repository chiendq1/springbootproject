package org.example.springbootproject.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.example.springbootproject.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;

import java.lang.reflect.Field;

public class FieldDuplicateValidator implements ConstraintValidator<FieldDuplicate, Object> {

    @Autowired
    private UserService userService;

    private String fieldName;
    private String idField;

    @Override
    public void initialize(FieldDuplicate constraintAnnotation) {
        fieldName = constraintAnnotation.fieldName();
        idField = constraintAnnotation.idField();
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        try {
            Field field = value.getClass().getDeclaredField(this.fieldName);
            Field idField = value.getClass().getDeclaredField(this.idField);
            field.setAccessible(true);
            idField.setAccessible(true);
            String fieldValue = (String) field.get(value);
            Integer id = (Integer) idField.get(value);

            // Check if the field already exists, either ignoring or not ignoring the ID
            boolean exists = userService.checkFieldDuplicated(this.fieldName, fieldValue, id);

            return !exists;
        } catch (Exception e) {
            return false;
        }
    }
}

