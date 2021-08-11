package com.luisjrz96.todolist.models.validators.task;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDate;

public class StringDateValidator implements ConstraintValidator<StringDateValidation, String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        try {
            LocalDate.parse(value);
            return true;
        }catch (Exception ex) {
            return false;
        }
    }
}
