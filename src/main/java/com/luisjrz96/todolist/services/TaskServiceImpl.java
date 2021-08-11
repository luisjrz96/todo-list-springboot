package com.luisjrz96.todolist.services;

import com.luisjrz96.todolist.exceptions.ElementNotFoundException;
import com.luisjrz96.todolist.exceptions.InternalServiceException;
import com.luisjrz96.todolist.models.Task;
import com.luisjrz96.todolist.repositories.TaskRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;
import org.hibernate.exception.DataException;

@Slf4j
@AllArgsConstructor
@Service
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;

    @Override
    public List<Task> findAllByStartingDate(LocalDate date, Pageable pageable) {

        List<Task> tasksByDate  = null;
        try {
            tasksByDate = taskRepository.findAllByStartingDate(date, pageable);
        } catch (DataException exception) {
            throw new InternalServiceException(exception.getMessage(), exception);
        }
        return tasksByDate;
    }

    @Override
    public Task createTask(Task task) {
        task.setId(null);
        Task savedTask = null;
        try {
            savedTask = taskRepository.save(task);
        } catch (DataException exception) {
            throw new InternalServiceException(exception.getMessage(), exception);
        }
        return savedTask;
    }

    @Override
    public Task updateTask(Task task, Long id) {
        Task updatedTask = null;
        try{
            if(taskRepository.findById(id).orElse(null) == null) {
                throw new ElementNotFoundException(String.valueOf(id), Task.class);
            }
            task.setId(id);
            updatedTask = taskRepository.save(task);
        } catch (DataException exception) {
            throw new InternalServiceException(exception.getMessage(), exception);
        }
        return updatedTask;
    }

    @Override
    public void delete(Long id) {
        try {
            Task task = taskRepository.findById(id).orElse(null);
            if(task == null) {
                throw new ElementNotFoundException(String.valueOf(id), Task.class);
            }
            taskRepository.delete(task);
        } catch (DataException exception) {
            throw new InternalServiceException(exception.getMessage(), exception);
        }
    }
}
