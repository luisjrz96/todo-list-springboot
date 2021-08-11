package com.luisjrz96.todolist.models.validators.task;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import javax.validation.ConstraintValidatorContext;

public class StringDateValidatorTests {


    private StringDateValidator stringDateValidator = new StringDateValidator();
    ConstraintValidatorContext constraintVC = Mockito.mock(ConstraintValidatorContext.class);

    @Test
    public void testStringDateValidator() {
        Assertions.assertTrue(stringDateValidator.isValid("2020-01-01", constraintVC));
        Assertions.assertTrue(stringDateValidator.isValid("2020-12-31", constraintVC));
        Assertions.assertFalse(stringDateValidator.isValid("2020-12", constraintVC));
    }
}
