package com.mansur.todo.controller;

import com.mansur.todo.entity.Todo;
import com.mansur.todo.repository.TodoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/todos")
public class TodoController {

    @Autowired
    private TodoRepository todoRepository;

    // Helper: get currently logged-in username from JWT
    private String getCurrentUsername() {
        return SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();
    }

    // ─── GET ALL MY TODOS ─────────────────────────────────────
    @GetMapping
    public List<Todo> getMyTodos() {
        String username = getCurrentUsername();
        return todoRepository.findByUsername(username); // only your todos
    }

    @GetMapping("/mansur")
    public String hello() {
        return "Hello, Mansur!";
    }
    // ─── CREATE TODO ──────────────────────────────────────────
    @PostMapping
    public Todo createTodo(@RequestBody Todo request) {
        String username = getCurrentUsername();

        Todo todo = new Todo();
        todo.setTitle(request.getTitle());
        todo.setCompleted(false);
        todo.setUsername(username); // attach to logged-in user

        return todoRepository.save(todo);
    }

    // ─── MARK COMPLETE ────────────────────────────────────────
    @PutMapping("/{id}/complete")
    public ResponseEntity<String> completeTodo(@PathVariable Long id) {
        String username = getCurrentUsername();

        Todo todo = todoRepository.findById(id)
                .orElse(null);

        if (todo == null) {
            return ResponseEntity.notFound().build();
        }

        // Make sure the todo belongs to the logged-in user
        if (!todo.getUsername().equals(username)) {
            return ResponseEntity.status(403).body("Not your todo!");
        }

        todo.setCompleted(true);
        todoRepository.save(todo);
        return ResponseEntity.ok("Todo marked complete ✅");
    }

    // ─── DELETE TODO ──────────────────────────────────────────
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteTodo(@PathVariable Long id) {
        String username = getCurrentUsername();

        Todo todo = todoRepository.findById(id)
                .orElse(null);

        if (todo == null) {
            return ResponseEntity.notFound().build();
        }

        // Make sure the todo belongs to the logged-in user
        if (!todo.getUsername().equals(username)) {
            return ResponseEntity.status(403).body("Not your todo!");
        }

        todoRepository.delete(todo);
        return ResponseEntity.ok("Todo deleted ✅");
    }
}
