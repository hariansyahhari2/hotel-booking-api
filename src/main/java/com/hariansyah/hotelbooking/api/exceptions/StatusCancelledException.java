package com.hariansyah.hotelbooking.api.exceptions;

import org.springframework.http.HttpStatus;

public class StatusCancelledException extends ApplicationException {

    public StatusCancelledException() {
        super(HttpStatus.valueOf(400), "error." + HttpStatus.valueOf(400).value() + ".status_cancelled");
    }
}
