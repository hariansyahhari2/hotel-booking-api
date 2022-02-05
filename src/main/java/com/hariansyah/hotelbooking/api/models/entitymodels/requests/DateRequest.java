package com.hariansyah.hotelbooking.api.models.entitymodels.requests;

import java.time.LocalDate;

public class DateRequest {
    private LocalDate startingDate;

    private LocalDate targetDate;

    public LocalDate getStartingDate() {
        return startingDate;
    }

    public void setStartingDate(LocalDate startingDate) {
        this.startingDate = startingDate;
    }

    public LocalDate getTargetDate() {
        return targetDate;
    }

    public void setTargetDate(LocalDate targetDate) {
        this.targetDate = targetDate;
    }
}
