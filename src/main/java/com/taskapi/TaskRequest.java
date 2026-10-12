package com.taskapi;

import jakarta.validation.constraints.NotBlank;

/**
 * Request body for creating a task.
 */
public class TaskRequest {

    @NotBlank(message = "Title must not be blank")
    private String title;

    private String description;

    public String getTitle()               { return title; }
    public void setTitle(String title)     { this.title = title; }

    public String getDescription()                     { return description; }
    public void setDescription(String description)     { this.description = description; }
}
