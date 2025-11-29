package com.trades.companies.services;

import com.trades.companies.exceptions.CompanyNotFoundException;
import com.trades.companies.models.Company;
import com.trades.companies.repositories.CompanyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CompanyService {
    private final CompanyRepository companyRepository;

    public List<Company> getCompanies() {
        return companyRepository.findAll();
    }

    public Company findBySymbol(String symbol) {
        return companyRepository.findBySymbol(symbol).orElseThrow(() -> new CompanyNotFoundException("Company not found"));
    }
}
