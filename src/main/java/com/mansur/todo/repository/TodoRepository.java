package com.mansur.todo.repository;

import com.mansur.todo.entity.Todo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TodoRepository extends JpaRepository<Todo, Long> {
    // Get all todos belonging to a specific user
    List<Todo> findByUsername(String username);
}
