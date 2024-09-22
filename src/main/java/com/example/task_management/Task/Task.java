package com.example.task_management.Task;

import com.example.task_management.User.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
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

    @Enumerated(EnumType.STRING)
    @Schema(description = "Statusul task-ului", example = "TODO", allowableValues = {"TODO", "IN_PROGRESS", "DONE"})
    @Column(nullable = false)
    private TaskStatus status;



    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // Relație Many-to-Many cu User
    @ManyToMany(fetch = FetchType.EAGER) //imi incarca datele deja in app din db
    @JoinTable(
            name = "task_shared", // Tabel intermediar -jpa genereaza autoamt tabel (COOL)
            joinColumns = @JoinColumn(name = "task_id"), // Coloana pentru Tasks
            inverseJoinColumns = @JoinColumn(name = "user_id") // Coloana pentru Users
    )
    private Set<User> sharedUsers;





    public Task() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        this.status = TaskStatus.TODO;
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
    public TaskStatus getstatus() {
        return status;
    }

    @JsonProperty("status")
    public void setstatus(TaskStatus status) {
        this.status = status;
    }


   @JsonProperty("owner_id")
    public User getuser() {
        return user;
    }

    @JsonProperty("owner_id")
    public void setuser(User user) {
        this.user = user;
    }

    @JsonProperty("shared_users")
    public Set<User> getsharedusers() {
        return sharedUsers;
    }

    @JsonProperty("shared_users")
    public void setsharedusers(Set<User> sharedusers) {
        this.sharedUsers = sharedusers;
    }
}
