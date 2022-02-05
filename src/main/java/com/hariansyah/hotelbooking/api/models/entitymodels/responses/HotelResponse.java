package com.hariansyah.hotelbooking.api.models.entitymodels.responses;

public class HotelResponse {

    private Integer id;

    private String name;

    private String about;

    private CompanyResponse company;

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

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public CompanyResponse getCompany() {
        return company;
    }

    public void setCompany(CompanyResponse company) {
        this.company = company;
    }

    public CityResponse getCity() {
        return city;
    }

    public void setCity(CityResponse city) {
        this.city = city;
    }
}
