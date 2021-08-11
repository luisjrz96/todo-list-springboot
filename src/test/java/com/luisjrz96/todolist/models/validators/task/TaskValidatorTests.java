package com.luisjrz96.todolist.models.validators.task;

import com.luisjrz96.todolist.models.Task;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import javax.validation.ConstraintValidatorContext;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Month;

public class TaskValidatorTests {

    private TaskValidator taskValidator = new TaskValidator();
    ConstraintValidatorContext constraintVC = Mockito.mock(ConstraintValidatorContext.class);


    @Test
    public void testTaskValidator() {
        Task task1 = new Task(1L, "a", "a", LocalDate.of(2020, Month.APRIL, 1),
                LocalDate.of(2020, Month.APRIL, 2), LocalTime.of(10,10, 10), LocalTime.of(11,11, 11));

        Task task2 = new Task(2L, "a", "a", LocalDate.of(2020, Month.APRIL, 3),
                LocalDate.of(2020, Month.APRIL, 2), LocalTime.of(10,10, 10), LocalTime.of(11,11, 11));


        Task task3 = new Task(3L, "a", "a", LocalDate.of(2020, Month.APRIL, 1),
                LocalDate.of(2020, Month.APRIL, 1), LocalTime.of(10,10, 10), LocalTime.of(11,11, 11));

        Task task4 = new Task(4L, "a", "a", LocalDate.of(2020, Month.APRIL, 1),
                LocalDate.of(2020, Month.APRIL, 1), LocalTime.of(12,10, 10), LocalTime.of(11,11, 11));

        Assertions.assertTrue(taskValidator.isValid(task1, constraintVC));
        Assertions.assertTrue(taskValidator.isValid(task3, constraintVC));
        Assertions.assertFalse(taskValidator.isValid(task2, constraintVC));
        Assertions.assertFalse(taskValidator.isValid(task4, constraintVC));
    }
}
