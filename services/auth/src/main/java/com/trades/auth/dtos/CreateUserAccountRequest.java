package com.trades.auth.dtos;

import lombok.Data;

import java.util.UUID;

@Data
public class CreateUserAccountRequest {
    private UUID userId;
}
