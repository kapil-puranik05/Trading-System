package com.trade.users.dtos;

import com.trade.users.util.Type;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
public class WalletUpdateDTO {
    private UUID userId;
    private BigDecimal cost;
    private Type type;
    private String reason;
}
