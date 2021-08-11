package com.luisjrz96.todolist.models.validators.task;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Constraint(validatedBy = TaskValidator.class)
@Target({ TYPE, FIELD, ANNOTATION_TYPE, PARAMETER })
@Retention(RUNTIME)
@Documented
public @interface TaskValidation {
    String message() default "Please provide valid dates for the task, startingDate should be first than endingDate";
    Class <?> [] groups() default {};
    Class <? extends Payload> [] payload() default {};
}
