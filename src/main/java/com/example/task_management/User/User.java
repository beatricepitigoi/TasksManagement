package com.example.task_management.User;

import com.example.task_management.Task.Task;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.util.HashSet;
import java.util.Set;

@JsonIgnoreProperties({"roles"})
@Entity
@Table(name = "users")
public class User  {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String username;

    @NotBlank(message = "Password is mandatory")
    @Size(min = 8, message = "Password must be at least 8 characters long")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$",
            message = "Password must contain at least one uppercase letter, one lowercase letter, one number, and one special character")
    @Column(nullable = false)
    private String password;

    @Email(message = "Invalid email address")
    @Column(unique = true)
    private String email;



    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "role")
    private Set<String> roles = new HashSet<>();

    @JsonProperty("roles")
    public Set<String> getroles() {
        return this.roles;
    }

    @JsonProperty("roles")
    public void setroles(HashSet<String> roles) {
        this.roles = roles;
    }



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
