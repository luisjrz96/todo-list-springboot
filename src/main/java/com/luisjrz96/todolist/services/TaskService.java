package com.luisjrz96.todolist.services;

import com.luisjrz96.todolist.models.Task;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;

public interface TaskService {

    public List<Task> findAllByStartingDate(LocalDate date, Pageable pageable);
    public Task createTask(Task task);
    public Task updateTask(Task task, Long id);
    public void delete(Long id);

}
