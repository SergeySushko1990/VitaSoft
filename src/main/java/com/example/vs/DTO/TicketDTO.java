package com.example.vs.DTO;


import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.sql.Timestamp;

@Data
public class TicketDTO {

    private Long id;

    @NotEmpty(message = "Theme should be not empty")
    private String theme;

    @NotEmpty(message = "Message should be not empty")
    private String msg;

    private Timestamp createdTime;

    private String status;

    private long author;
}
