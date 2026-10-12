package com.taskapi;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

@RestController
public class TaskController {

    // Thread-safe in-memory store
    private final List<Task> tasks = new ArrayList<>();
    private final AtomicLong counter = new AtomicLong(1);

    // ── GET / ─────────────────────────────────────────────────────────────────
    @GetMapping("/")
    public Map<String, Object> home() {
        return Map.of(
            "message", "Welcome to the Java Task API!",
            "version", "1.0.0",
            "endpoints", Map.of(
                "GET  /",            "This help message",
                "GET  /health",      "Health check",
                "GET  /tasks",       "List all tasks",
                "POST /tasks",       "Create a task  { title, description }",
                "GET  /tasks/{id}",  "Get task by ID",
                "PUT  /tasks/{id}",  "Update task (mark done)",
                "DELETE /tasks/{id}","Delete task by ID"
            )
        );
    }

    // ── GET /health ───────────────────────────────────────────────────────────
    @GetMapping("/health")
    public Map<String, String> health() {
        return Map.of(
            "status",    "healthy",
            "timestamp", Instant.now().toString()
        );
    }

    // ── GET /tasks ────────────────────────────────────────────────────────────
    @GetMapping("/tasks")
    public Map<String, Object> getTasks() {
        return Map.of("tasks", tasks, "count", tasks.size());
    }

    // ── POST /tasks ───────────────────────────────────────────────────────────
    @PostMapping("/tasks")
    public ResponseEntity<Task> createTask(@Valid @RequestBody TaskRequest request) {
        Task task = new Task(
            counter.getAndIncrement(),
            request.getTitle(),
            request.getDescription() != null ? request.getDescription() : ""
        );
        tasks.add(task);
        return ResponseEntity.status(HttpStatus.CREATED).body(task);
    }

    // ── GET /tasks/{id} ───────────────────────────────────────────────────────
    @GetMapping("/tasks/{id}")
    public Task getTask(@PathVariable long id) {
        return tasks.stream()
            .filter(t -> t.getId() == id)
            .findFirst()
            .orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND, "Task " + id + " not found"
            ));
    }

    // ── PUT /tasks/{id} ───────────────────────────────────────────────────────
    @PutMapping("/tasks/{id}")
    public Task updateTask(@PathVariable long id, @RequestBody Map<String, Object> body) {
        Task task = tasks.stream()
            .filter(t -> t.getId() == id)
            .findFirst()
            .orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND, "Task " + id + " not found"
            ));

        if (body.containsKey("title"))       task.setTitle((String) body.get("title"));
        if (body.containsKey("description")) task.setDescription((String) body.get("description"));
        if (body.containsKey("done"))        task.setDone((Boolean) body.get("done"));

        return task;
    }

    // ── DELETE /tasks/{id} ────────────────────────────────────────────────────
    @DeleteMapping("/tasks/{id}")
    public Map<String, String> deleteTask(@PathVariable long id) {
        boolean removed = tasks.removeIf(t -> t.getId() == id);
        if (!removed) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Task " + id + " not found");
        }
        return Map.of("message", "Task " + id + " deleted successfully");
    }
}
