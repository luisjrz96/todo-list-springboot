package com.luisjrz96.todolist.controllers;

import com.luisjrz96.todolist.models.Task;
import com.luisjrz96.todolist.models.validators.task.StringDateValidation;
import com.luisjrz96.todolist.models.validators.task.TaskValidation;
import com.luisjrz96.todolist.services.TaskService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@Validated
@RestController
@RequestMapping("/api/tasks")
@AllArgsConstructor
public class TaskController {

    private final TaskService taskService;

    @GetMapping
    public List<com.luisjrz96.todolist.models.Task> findTasksByDate(
            @StringDateValidation
            @RequestParam("date") String date,
            @RequestParam("page") int page,
            @RequestParam("size") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return taskService.findAllByStartingDate(LocalDate.parse(date), pageable);
    }

    @PostMapping
    public com.luisjrz96.todolist.models.Task createTask(
            @TaskValidation
            @RequestBody Task task) {
        return taskService.createTask(task);
     }


    @PutMapping("/{id}")
    public com.luisjrz96.todolist.models.Task updateTask(@RequestBody Task task, @PathVariable Long id) {
        return taskService.updateTask(task, id);
    }

    @DeleteMapping("/{id}")
    public void deleteTask(@PathVariable Long id) {
        taskService.delete(id);
    }


}
