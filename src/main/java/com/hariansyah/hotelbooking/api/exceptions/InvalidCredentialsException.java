package com.hariansyah.hotelbooking.api.exceptions;

import org.springframework.http.HttpStatus;

public class InvalidCredentialsException extends ApplicationException {

    public InvalidCredentialsException() {
        super(HttpStatus.valueOf(400), "error." + HttpStatus.valueOf(400).value() + ".invalid_credentials");
    }
}
