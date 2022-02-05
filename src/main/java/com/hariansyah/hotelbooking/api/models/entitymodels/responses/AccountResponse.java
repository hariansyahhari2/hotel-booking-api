package com.hariansyah.hotelbooking.api.models.entitymodels.responses;

import com.hariansyah.hotelbooking.api.enums.RoleEnum;

public class AccountResponse {

    private Integer id;

    private String username;

    private String email;

    private RoleEnum role;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public RoleEnum getRole() {
        return role;
    }

    public void setRole(RoleEnum role) {
        this.role = role;
    }
}
