package com.trades.companies.dtos;

import com.trades.companies.util.Status;
import lombok.Data;

@Data
public class CompanyRequestDTO {
    private String symbol;
    private Status status;
}
