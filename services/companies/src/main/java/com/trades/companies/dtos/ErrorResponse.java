package com.trades.companies.dtos;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ErrorResponse {
    public String message;
    public LocalDateTime time;
}
