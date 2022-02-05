package com.hariansyah.hotelbooking.api.exceptions;

import org.springframework.http.HttpStatus;

public class InvalidPermissionsException extends ApplicationException {

    public InvalidPermissionsException() {
        super(HttpStatus.valueOf(400), "error." + HttpStatus.valueOf(400).value() + ".invalid_permissions");
    }
}
