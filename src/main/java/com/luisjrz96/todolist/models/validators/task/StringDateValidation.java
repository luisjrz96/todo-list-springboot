package com.luisjrz96.todolist.models.validators.task;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({ TYPE, FIELD, ANNOTATION_TYPE, PARAMETER })
@Retention(RUNTIME)
@Constraint(validatedBy = StringDateValidator.class)
public @interface StringDateValidation {
    String message() default "Please provide a valid date with the format yyyy-MM-dd";
    Class <?> [] groups() default {};
    Class <? extends Payload> [] payload() default {};
}
