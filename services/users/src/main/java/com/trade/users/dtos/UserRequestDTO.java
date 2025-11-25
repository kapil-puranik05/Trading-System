package com.trade.users.dtos;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class UserRequestDTO {
    private String name;
    private String password;
    private String email;
    private BigDecimal walletBalance;
}
