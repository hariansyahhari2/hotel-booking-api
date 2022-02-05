package com.hariansyah.hotelbooking.api.exceptions;

import org.springframework.http.HttpStatus;

public class DateInvalidException extends ApplicationException {

    public DateInvalidException() {
        super(HttpStatus.valueOf(400), "error." + HttpStatus.valueOf(400).value() + ".date-invalid");
    }
}
