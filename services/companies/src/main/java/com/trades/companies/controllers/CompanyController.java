package com.trades.companies.controllers;

import com.trades.companies.services.CompanyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/companies")
@RequiredArgsConstructor
public class CompanyController {
    private final CompanyService companyService;

    @GetMapping
    public ResponseEntity<?> getAllCompanies() {
        return new ResponseEntity<>(companyService.getCompanies(), HttpStatus.OK);
    }

    @GetMapping("/{symbol}")
    public ResponseEntity<?> getCompanyBySymbol(@PathVariable String symbol) {
        return new ResponseEntity<>(companyService.findBySymbol(symbol), HttpStatus.OK);
    }
}
