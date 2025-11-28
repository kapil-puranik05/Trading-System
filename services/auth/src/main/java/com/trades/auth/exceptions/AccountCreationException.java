package com.trades.auth.exceptions;

public class AccountCreationException extends RuntimeException {
    public AccountCreationException(String message) {
        super(message);
    }
}
