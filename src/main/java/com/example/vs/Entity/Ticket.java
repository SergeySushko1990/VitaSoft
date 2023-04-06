package com.example.vs.Entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import org.hibernate.annotations.BatchSize;

import java.sql.Timestamp;

@Entity
@Data
@Table(name = "tickets")
public class Ticket {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @Column(name = "theme")
    @NotEmpty(message = "Theme should be not empty")
    private String theme;

    @Column(name = "msg")
    @NotEmpty(message = "Message should be not empty")
    private String msg;

    @Column(name = "created_time")
    private Timestamp createdTime;

    @Column(name = "status")
    private String status;

    @Column(name = "author")
    private long author;

}
