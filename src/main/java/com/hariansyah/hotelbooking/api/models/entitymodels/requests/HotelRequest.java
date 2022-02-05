package com.hariansyah.hotelbooking.api.models.entitymodels.requests;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class HotelRequest {

    @NotBlank
    private String name;

    @NotBlank
    private String about;

    @NotNull
    private Integer companyId;

    @NotNull
    private Integer cityId;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public Integer getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }

    public Integer getCityId() {
        return cityId;
    }

    public void setCityId(Integer cityId) {
        this.cityId = cityId;
    }
}
