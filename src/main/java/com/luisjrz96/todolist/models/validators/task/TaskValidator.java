package com.luisjrz96.todolist.models.validators.task;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class TaskValidator implements ConstraintValidator<TaskValidation, com.luisjrz96.todolist.models.Task> {

    @Override
    public boolean isValid(com.luisjrz96.todolist.models.Task task, ConstraintValidatorContext context) {
        if (task.getEndingDate().compareTo(task.getStartingDate()) >= 0) {
            if (task.getEndingDate().compareTo(task.getStartingDate()) == 0) {
                return task.getEndingTime().compareTo(task.getStartingTime()) > 0;
            }
            return true;
        } else {
            return false;
        }
    }
}
