package com.example.vs.Exceptions;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TicketErrorResponse {
    private String message;
    private Long timestamp;

}
