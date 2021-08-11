package com.luisjrz96.todolist.repositories;


import com.luisjrz96.todolist.models.Task;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface TaskRepository extends PagingAndSortingRepository<Task, Long> {
    List<Task> findAllByStartingDate(LocalDate date, Pageable pageable);
}
