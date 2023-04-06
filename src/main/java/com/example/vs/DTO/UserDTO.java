package com.example.vs.DTO;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.sql.Timestamp;

@Data
public class UserDTO {

    private Long id;

    @NotEmpty(message = "Username should be not empty")
    private String username;

    @NotEmpty(message = "Password should be not empty")
    private String password;

    @NotEmpty(message = "Name should be not empty")
    private String name;

    private Timestamp time;

    private String role;
}
