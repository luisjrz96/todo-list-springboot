package com.luisjrz96.todolist.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.luisjrz96.todolist.exceptions.ElementNotFoundException;
import com.luisjrz96.todolist.exceptions.InternalServiceException;
import com.luisjrz96.todolist.models.Task;
import com.luisjrz96.todolist.repositories.TaskRepository;
import com.luisjrz96.todolist.services.TaskServiceImpl;
import org.hibernate.exception.DataException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class TaskControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TaskServiceImpl taskService;

    @MockBean
    private TaskRepository taskRepository;

    private ObjectMapper objectMapper;

    @BeforeAll
    public void setup() {
        objectMapper = new ObjectMapper();
        objectMapper.findAndRegisterModules();
    }


    @Test
    public void findAllTasks_Success() throws Exception {
        //GIVEN
        List<Task> tasks = Arrays.stream(objectMapper.readValue(ClassLoader.getSystemResourceAsStream("payloads/task_list.json"),
                Task[].class)).collect(Collectors.toList());

        //WHEN
        Mockito.when(taskRepository.findAllByStartingDate(LocalDate.of(2020,4,14), PageRequest.of(0, 10)))
                        .thenReturn(tasks);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/tasks")
                        .param("date", tasks.get(0).getStartingDate().toString())
                        .param("page",String.valueOf(0))
                        .param("size", String.valueOf(10))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }



    @Test
    public void findAllTasks_FailInternalServerError() throws Exception {
        //GIVEN
        List<Task> tasks = Arrays.stream(objectMapper.readValue(ClassLoader.getSystemResourceAsStream("payloads/task_list.json"),
                Task[].class)).collect(Collectors.toList());

        //WHEN
        Mockito.when(taskService.findAllByStartingDate(LocalDate.of(2020,4,14), PageRequest.of(0, 10)))
                .thenThrow(new InternalServiceException("ERROR", new SQLException("FOO")));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/tasks")
                        .param("date", tasks.get(0).getStartingDate().toString())
                        .param("page",String.valueOf(0))
                        .param("size", String.valueOf(10))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isInternalServerError());
    }

    @Test
    public void saveTask_SuccessTest() throws Exception {
        //GIVEN
        Task task = objectMapper.readValue(ClassLoader.getSystemResourceAsStream("payloads/create_task.json"),
                Task.class);
        String json = objectMapper.writeValueAsString(task);

        //WHEN
        Mockito.when(taskRepository.save(task)).thenReturn(task);

        //THEN
        mockMvc.perform(MockMvcRequestBuilders.post("/api/tasks")
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }


    @Test
    public void saveTask_FailConstraintValidationException() throws Exception {
        //GIVEN
        Task task = objectMapper.readValue(ClassLoader.getSystemResourceAsStream("payloads/invalid_task.json"),
                Task.class);
        String json = objectMapper.writeValueAsString(task);

        //WHEN
        Mockito.when(taskRepository.save(task)).thenReturn(task);

        //THEN
        mockMvc.perform(MockMvcRequestBuilders.post("/api/tasks")
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }


    @Test
    public void saveTask_Fail_InternalServiceExceptionTest() throws Exception {
        //GIVEN
        Task task = objectMapper.readValue(ClassLoader.getSystemResourceAsStream("payloads/create_task.json"),
                Task.class);
        String json = objectMapper.writeValueAsString(task);

        //WHEN
        Mockito.when(taskService.createTask(task))
                .thenThrow(new InternalServiceException("Error", new DataException("Error",
                new SQLException("Error Saving Task"))));

        //THEN
        mockMvc.perform(MockMvcRequestBuilders.post("/api/tasks")
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isInternalServerError());
    }

    @Test
    public void updateTask_SuccessTest() throws Exception {
        //GIVEN
        Task task = objectMapper.readValue(ClassLoader.getSystemResourceAsStream("payloads/create_task.json"),
                Task.class);

        //WHEN
        Mockito.when(taskRepository.findById(task.getId())).thenReturn(Optional.of(task));
        task.setSubject("updated subject");
        String json = objectMapper.writeValueAsString(task);
        Mockito.when(taskRepository.save(task)).thenReturn(task);

        //THEN
        mockMvc.perform(MockMvcRequestBuilders.put("/api/tasks/".concat(task.getId().toString()))
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }



    @Test
    public void updateTask_Fail_ElementNotFoundException() throws Exception {
        //GIVEN
        Task task = objectMapper.readValue(ClassLoader.getSystemResourceAsStream("payloads/create_task.json"),
                Task.class);
        String json = objectMapper.writeValueAsString(task);

        //WHEN
        Mockito.when(taskService.updateTask(task, task.getId())).thenThrow(new ElementNotFoundException(
                "Element Not found", new SQLException("Element not available")));

        //THEN
        mockMvc.perform(MockMvcRequestBuilders.put("/api/tasks/".concat(task.getId().toString()))
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    public void updateTask_Fail_InternalServiceException() throws Exception {
        //GIVEN
        Task task = objectMapper.readValue(ClassLoader.getSystemResourceAsStream("payloads/create_task.json"),
                Task.class);
        String json = objectMapper.writeValueAsString(task);

        //WHEN
        Mockito.when(taskService.updateTask(task, task.getId())).thenThrow(new InternalServiceException(
                "Error updating element", new SQLException("DB connection error")));

        //THEN
        mockMvc.perform(MockMvcRequestBuilders.put("/api/tasks/".concat(task.getId().toString()))
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isInternalServerError());
    }


    @Test
    public void deleteTask_SuccessTest() throws Exception {
        //GIVEN
        Task task = objectMapper.readValue(ClassLoader.getSystemResourceAsStream("payloads/create_task.json"),
                Task.class);
        String json = objectMapper.writeValueAsString(task);

        //WHEN
        Mockito.when(taskRepository.findById(task.getId())).thenReturn(Optional.of(task));

        //THEN
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/tasks/".concat(task.getId().toString()))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }



    @Test
    public void deleteTask_Fail_ElementNotFoundException() throws Exception {
        //GIVEN
        Task task = objectMapper.readValue(ClassLoader.getSystemResourceAsStream("payloads/create_task.json"),
                Task.class);
        String json = objectMapper.writeValueAsString(task);

        //WHEN
        Mockito.doThrow(new ElementNotFoundException(task.getId().toString(),
                Task.class)).when(taskService).delete(task.getId());
        //THEN
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/tasks/".concat(task.getId().toString()))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }


    @Test
    public void deleteTask_Fail_InternalServerError() throws Exception {
        //GIVEN
        Task task = objectMapper.readValue(ClassLoader.getSystemResourceAsStream("payloads/create_task.json"),
                Task.class);
        String json = objectMapper.writeValueAsString(task);

        //WHEN
        Mockito.doThrow(new InternalServiceException("Error", new SQLException("Error in db connection"))).when(taskService).delete(task.getId());
        //THEN
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/tasks/".concat(task.getId().toString()))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isInternalServerError());
    }




}
