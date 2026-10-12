package com.taskapi;

import jakarta.validation.constraints.NotBlank;
import java.time.Instant;

public class Task {

    private long id;

    @NotBlank(message = "Title must not be blank")
    private String title;

    private String description;

    private boolean done;

    private Instant createdAt;

    // ── Constructors ──────────────────────────────────────────────────────────

    public Task() {}

    public Task(long id, String title, String description) {
        this.id          = id;
        this.title       = title;
        this.description = description;
        this.done        = false;
        this.createdAt   = Instant.now();
    }

    // ── Getters & Setters ─────────────────────────────────────────────────────

    public long getId()                    { return id; }
    public void setId(long id)             { this.id = id; }

    public String getTitle()               { return title; }
    public void setTitle(String title)     { this.title = title; }

    public String getDescription()                     { return description; }
    public void setDescription(String description)     { this.description = description; }

    public boolean isDone()                { return done; }
    public void setDone(boolean done)      { this.done = done; }

    public Instant getCreatedAt()                  { return createdAt; }
    public void setCreatedAt(Instant createdAt)    { this.createdAt = createdAt; }
}
