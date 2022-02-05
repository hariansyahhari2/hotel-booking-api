package com.hariansyah.hotelbooking.api.models.entitymodels.requests;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class CityRequest {

    @NotBlank
    private String name;

    @NotNull
    private Integer regionId;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getRegionId() {
        return regionId;
    }

    public void setRegionId(Integer regionId) {
        this.regionId = regionId;
    }
}
