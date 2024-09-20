package com.example.task_management.Task.DTO;

import com.example.task_management.Task.TaskStatus;
import com.fasterxml.jackson.annotation.JsonProperty;

public class TaskDTO {

    private String title;
    private String description;
    private TaskStatus status;
    private Long ownerId;

    @JsonProperty("title")
    public String gettitle() {
        return title;
    }

    @JsonProperty("title")
    public void settitle(String title) {
        this.title = title;
    }

    @JsonProperty("description")
    public String getdescription() {
        return description;
    }

    @JsonProperty("description")
    public void setdescription(String description) {
        this.description = description;
    }

    @JsonProperty("status")
    public TaskStatus getstatus() {
        return status;
    }

    @JsonProperty("status")
    public void setstatus(TaskStatus status) {
        this.status = status;
    }


    @JsonProperty("owner_id")
    public Long getownerid() {
        return ownerId;
    }

    @JsonProperty("owner_id")
    public void setownerid(Long ownerId) {
        this.ownerId = ownerId;
    }
}

