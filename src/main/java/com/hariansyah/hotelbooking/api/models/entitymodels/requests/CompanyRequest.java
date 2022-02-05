package com.hariansyah.hotelbooking.api.models.entitymodels.requests;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class CompanyRequest {

    @NotBlank
    private String name;

    @NotNull
    private Integer cityId;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getCityId() {
        return cityId;
    }

    public void setCityId(Integer cityId) {
        this.cityId = cityId;
    }
}
