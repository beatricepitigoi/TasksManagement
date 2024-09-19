package com.example.task_management;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;


import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Table(name = "tasks")
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = true)
    private String description;

    @Column(name = "created_at", nullable = false)
    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = true)
    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    private LocalDateTime updatedAt;

    @Column(nullable = false)
    private String status;

    @ElementCollection
    @CollectionTable(name = "taskShared", joinColumns = @JoinColumn(name = "task_id"))
    @Column(name = "user_id")
    private Set<Long> sharedUsers;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;


    public Task() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        this.status = String.valueOf(TaskStatus.OPENED);
    }



    // --------------------------- Getteri și setteri --------------------------------
    @JsonProperty("id")
    public Long getid() {
        return id;
    }

    @JsonProperty("id")
    public void setid(Long id) {
        this.id = id;
    }

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

    @JsonProperty("created_at")
    public LocalDateTime getcreatedAt() {
        return createdAt;
    }


    @JsonProperty("created_at")
    public void setcreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }


    @JsonProperty("updated_at")
    public LocalDateTime getupdatedAt() {
        return updatedAt;
    }


    @JsonProperty("updated_at")
    public void setupdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }


    @JsonProperty("status")
    public String getstatus() {
        return status;
    }

    @JsonProperty("status")
    public void setstatus(String status) {
        this.status = status;
    }

    public Set<Long> getsharedUsers() {
        return sharedUsers;
    }

    public void setsharedUsers(Set<Long> sharedUsers) {
        this.sharedUsers = sharedUsers;
    }

    public User getuser() {
        return user;
    }

    public void setuser(User user) {
        this.user = user;
    }
}
