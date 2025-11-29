package com.trades.companies.models;

import com.trades.companies.util.Status;
import jakarta.persistence.*;
import lombok.Data;

import java.util.UUID;

@Entity
@Table(name = "companies")
@Data
public class Company {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID companyId;

    @Column(nullable = false, unique = true)
    private String symbol;

    @Column(nullable = false)
    private Status status;
}
