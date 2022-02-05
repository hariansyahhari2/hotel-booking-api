package com.hariansyah.hotelbooking.api.models.entitymodels.responses;

public class CityResponse {

    private Integer id;

    private String name;

    private RegionResponse region;

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

    public RegionResponse getRegion() {
        return region;
    }

    public void setRegion(RegionResponse region) {
        this.region = region;
    }
}
