package it.pierlorenzo.ecommerce.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class ErroreResponse {

    private int status;
    private String error;
    private String message;
    private LocalDateTime timestamp;
}
