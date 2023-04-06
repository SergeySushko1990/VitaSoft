package com.example.vs.Entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.sql.Timestamp;
import java.util.Collection;

@Entity
@Data
@Table(name = "users")
public class Users {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @Column(name = "username")
    @NotEmpty(message = "Username should be not empty")
    private String username;

    @Column(name = "password")
    @NotEmpty(message = "Password should be not empty")
    private String password;

    @Column(name = "name")
    @NotEmpty(message = "Name should be not empty")
    private String name;

    @Column(name = "time")
    private Timestamp time;

    @Column(name = "role")
    private String role;

}
