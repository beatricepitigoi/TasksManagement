package com.example.task_management.User;

import com.example.task_management.Task.Task;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import java.util.Set;


@Entity
@Table(name = "users")
public class User  {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(unique = true)
    private String email;

    //Constructors
    public User() {}
    public User(String username, String password, String email) {
        this.username = username;
        this.password = password;
        this.email = email;
    }

    //Legatura Tasks
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Task> tasks;

    // Relație Many-to-Many inversă cu Task
    @ManyToMany(mappedBy = "sharedUsers") //Funny story: In mappedby: numele atributului din task, nu efectiv un tabel/coloana
    private Set<Task> sharedTasks;


    // Getters și Setters
    @JsonProperty("id")
    public Long getid() {
        return id;
    }

    public void setid(Long id) {
        this.id = id;
    }

    @JsonProperty("username")
    public String getusername() {
        return username;
    }

    @JsonProperty("username")
    public void setusername(String username) {
        this.username = username;
    }

    @JsonProperty("password")
    public String getpassword() {
        return password;
    }

    @JsonProperty("password")
    public void setpassword(String password) {
        this.password = password;
    }

    @JsonProperty("email")
    public String getemail() {
        return email;
    }

    @JsonProperty("email")
    public void setemail(String email) {
        this.email = email;
    }
}
