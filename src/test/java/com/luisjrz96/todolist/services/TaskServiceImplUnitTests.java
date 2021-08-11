package com.luisjrz96.todolist.services;

import com.luisjrz96.todolist.exceptions.ElementNotFoundException;
import com.luisjrz96.todolist.exceptions.InternalServiceException;
import com.luisjrz96.todolist.models.Task;
import com.luisjrz96.todolist.repositories.TaskRepository;
import org.hibernate.exception.DataException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Month;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class TaskServiceImplUnitTests {

    @Mock
    private TaskRepository taskRepository;

    @InjectMocks
    private TaskServiceImpl taskService;


    @Test
    public void testListAllTaskOK() {
        //GIVEN
        Task task1 = new Task(1L, "A1", "B1", LocalDate.of(2022,Month.APRIL, 30),
                LocalDate.of(2022,Month.APRIL, 30),
                LocalTime.of(11,0,0), LocalTime.of(12,0,0));

        Task task2 = new Task(1L, "A2", "B2",LocalDate.of(2022,Month.APRIL, 30),
                LocalDate.of(2022,Month.APRIL, 30), LocalTime.of(13,0,0),
                LocalTime.of(14,0,0));

        Task task3 = new Task(1L, "A3", "B3",LocalDate.of(2022,Month.APRIL, 30),
                LocalDate.of(2022,Month.APRIL, 30), LocalTime.of(16,0,0),
                LocalTime.of(23,59,59));

        //WHEN
        List<Task> tasks = List.of(task1, task2, task3);
        LocalDate date = LocalDate.of(2022, Month.APRIL, 30);
        Pageable pageable = PageRequest.of(0, 10);
        Mockito.when(taskRepository.findAllByStartingDate(date, pageable)).thenReturn(tasks);

        //THEN
        List<Task> taskList = taskService.findAllByStartingDate(date, pageable);

    }

    @Test
    public void testAddTaskOK() {
        // GIVEN
        Task task = new Task();
        task.setSubject("ABCD");
        task.setDescription("FGHIJ");
        task.setStartingDate(LocalDate.of(2020, Month.APRIL, 19));
        task.setStartingTime(LocalTime.of(11, 30, 0));
        task.setEndingTime(LocalTime.of(12, 0, 0));
        Task savedTask = task;
        savedTask.setId(1L);

        //WHEN
        Mockito.when(taskRepository.save(task)).thenReturn(savedTask);
        Task obtainedTask = taskService.createTask(task);

        //THEN
        Assertions.assertNotNull(obtainedTask);
        Assertions.assertEquals(savedTask, obtainedTask);
    }

    @Test
    public void testUpdateTaskOK() {
        // GIVEN
        String subject1 = "Subject 1";
        String subject2 = "Subject 2";

        Task task = new Task();
        task.setId(1L);
        task.setSubject(subject1);
        task.setDescription("FGHIJ");
        task.setStartingDate(LocalDate.of(2020, Month.APRIL, 19));
        task.setStartingTime(LocalTime.of(11, 30, 0));
        task.setEndingTime(LocalTime.of(12, 0, 0));

        //WHEN
        Mockito.when(taskRepository.findById(1L)).thenReturn(Optional.of(task));
        Task updatedTask = task;
        task.setSubject(subject2);
        Mockito.when(taskService.updateTask(updatedTask, 1L)).thenReturn(updatedTask);
        Task obtainedTask = taskService.updateTask(updatedTask, 1L);

        //THEN
        Assertions.assertEquals(obtainedTask.getSubject(), subject2);
    }

    @Test
    public void testUpdateTaskFailElementNotFoundException() {
        // GIVEN
        String subject1 = "Subject 1";

        Task task = new Task();
        task.setId(1L);
        task.setSubject(subject1);
        task.setDescription("FGHIJ");
        task.setStartingDate(LocalDate.of(2020, Month.APRIL, 19));
        task.setStartingTime(LocalTime.of(11, 30, 0));
        task.setEndingTime(LocalTime.of(12, 0, 0));

        //WHEN
        Mockito.when(taskRepository.findById(1L)).thenReturn(Optional.empty());

        //THEN
        Assertions.assertThrows(ElementNotFoundException.class, ()->{
            taskService.updateTask(task, 1L);
        });
    }

    @Test
    public void testDeleteTaskOK() {
        // GIVEN
        Task task = new Task();
        task.setId(1L);
        task.setSubject("ABCD");
        task.setDescription("FGHIJ");
        task.setStartingDate(LocalDate.of(2020, Month.APRIL, 19));
        task.setStartingTime(LocalTime.of(11, 30, 0));
        task.setEndingTime(LocalTime.of(12, 0, 0));

        //WHEN
        Mockito.when(taskRepository.findById(1L)).thenReturn(Optional.of(task));
        Mockito.doNothing().when(taskRepository).delete(task);

        //THEN
        taskService.delete(1L);
    }



    @Test
    public void testDeleteTaskFailElementNotFoundException() {
        //WHEN
        Mockito.when(taskRepository.findById(1L)).thenReturn(Optional.empty());

        //THEN
        Assertions.assertThrows(ElementNotFoundException.class, ()-> {
            taskService.delete(1L);
        });
    }

    @Test
    public void testListAllTaskFailInternalServiceException() {
        //WHEN
        LocalDate date = LocalDate.of(2022, Month.APRIL, 30);
        Pageable pageable = PageRequest.of(0, 10);
        Mockito.when(taskRepository.findAllByStartingDate(date, pageable))
                .thenThrow(new DataException("Error", new SQLException("Error Retrieving data")));

        //THEN
        Assertions.assertThrows(InternalServiceException.class, () -> {
            taskService.findAllByStartingDate(date, pageable);
        });

    }

    @Test
    public void testAddTaskFailInternalServiceException() {
        // GIVEN
        Task task = new Task();
        task.setSubject("ABCD");
        task.setDescription("FGHIJ");
        task.setStartingDate(LocalDate.of(2020, Month.APRIL, 19));
        task.setStartingTime(LocalTime.of(11, 30, 0));
        task.setEndingTime(LocalTime.of(12, 0, 0));

        //WHEN
        Mockito.when(taskRepository.save(task))
                .thenThrow(new DataException("Error", new SQLException("Error Saving data")));
        //THEN
        Assertions.assertThrows(InternalServiceException.class, ()-> {
           taskService.createTask(task);
        });
    }

    @Test
    public void testUpdateTaskFailInternalServiceException() {
        // GIVEN
        String subject1 = "Subject 1";
        String subject2 = "Subject 2";

        Task task = new Task();
        task.setId(1L);
        task.setSubject(subject1);
        task.setDescription("FGHIJ");
        task.setStartingDate(LocalDate.of(2020, Month.APRIL, 19));
        task.setStartingTime(LocalTime.of(11, 30, 0));
        task.setEndingTime(LocalTime.of(12, 0, 0));

        //WHEN
        Mockito.when(taskRepository.findById(1L)).thenReturn(Optional.of(task));
        Task updatedTask = task;
        task.setSubject(subject2);
        Mockito.when(taskRepository.save(updatedTask)).thenThrow(new DataException("Error", new SQLException("Error Saving data")));


        //THEN
        Assertions.assertThrows(InternalServiceException.class, ()-> {
            taskService.updateTask(updatedTask, 1L);
        });
    }


    @Test
    public void testDeleteTaskFailInternalServiceException() {
        // GIVEN
        Task task = new Task();
        task.setId(1L);
        task.setSubject("ABCD");
        task.setDescription("FGHIJ");
        task.setStartingDate(LocalDate.of(2020, Month.APRIL, 19));
        task.setStartingTime(LocalTime.of(11, 30, 0));
        task.setEndingTime(LocalTime.of(12, 0, 0));

        //WHEN
        Mockito.when(taskRepository.findById(1L)).thenReturn(Optional.of(task));
        Mockito.doThrow(new DataException("Error", new SQLException("Error Deleting element"))).when(taskRepository).delete(task);

        //THEN
        Assertions.assertThrows(InternalServiceException.class, ()-> {
            taskService.delete(1L);
        });
    }


}
