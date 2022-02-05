package com.hariansyah.hotelbooking.api.models.entitymodels.responses;

public class CompanyResponse {

    private Integer id;

    private String name;

    private CityResponse city;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public CityResponse getCity() {
        return city;
    }

    public void setCity(CityResponse city) {
        this.city = city;
    }
}
