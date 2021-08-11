package com.luisjrz96.todolist.repositories;

import com.luisjrz96.todolist.models.Task;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.jdbc.Sql;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Month;
import java.util.List;

@DataJpaTest
public class TaskRepositoryTests {

    @Autowired
    private TaskRepository taskRepository;


    @Test
    public void saveTaskTest() {
        Task task = new Task();
                task.setSubject("Go to the cinema");
                task.setDescription("Go to the cinema to see the new avengers movie");
                task.setStartingDate(LocalDate.of(2019, Month.APRIL, 22));
                task.setEndingDate(LocalDate.of(2019, Month.APRIL, 22));
                task.setStartingTime(LocalTime.of(23,59,59));
                task.setEndingTime(LocalTime.of(3,0,0));

        Long id = taskRepository.save(task).getId();
        Task taskFound = taskRepository.findById(id).orElse(null);
        Assertions.assertNotNull(taskFound);
    }


    @Test
    public void updateTaskTest() {
        //create a new record
        Task task = new Task();
        String firstSubject = "Go to the cinema";
        String secondSubject = "Go to the cinema with my girlfriend";
        task.setSubject(firstSubject);
        task.setDescription("Go to the cinema to see the new avengers movie");
        task.setStartingDate(LocalDate.of(2019, Month.APRIL, 22));
        task.setStartingTime(LocalTime.of(23,59,59));
        task.setEndingTime(LocalTime.of(3,0,0));

        Long id = taskRepository.save(task).getId();
        Task taskFound = taskRepository.findById(id).orElse(null);
        Assertions.assertEquals(task.getSubject(), firstSubject);
        Assertions.assertNotNull(taskFound);

        //Updating the record
        Task task2 = taskFound;
        task2.setSubject(secondSubject);
        taskRepository.save(task2);

        taskFound = taskRepository.findById(id).orElse(null);
        Assertions.assertEquals(task.getSubject(), secondSubject);


    }

    @Test
    @Sql("/scripts/addTasks.sql")
    public void listAllTheTaskTest() {
        List<Task> taskList = taskRepository.findAllByStartingDate(LocalDate.of(2021, 07 ,25), Pageable.ofSize(10));
        Assertions.assertNotEquals(taskList.size(), 0);
        Assertions.assertEquals(taskList.size(), 6);
    }

    @Test
    @Sql("/scripts/addTasks.sql")
    public void deleteTasksTest() {
        List<Task> taskList = taskRepository.findAllByStartingDate(LocalDate.of(2021, 07 ,25), Pageable.ofSize(10));
        Assertions.assertNotEquals(taskList.size(), 0);
        Assertions.assertEquals(taskList.size(), 6);
        taskRepository.delete(taskList.get(0));
        taskRepository.delete(taskList.get(1));
        taskList = taskRepository.findAllByStartingDate(LocalDate.of(2021, 07 ,25), Pageable.ofSize(10));
        Assertions.assertEquals(taskList.size(), 4);
    }

}
