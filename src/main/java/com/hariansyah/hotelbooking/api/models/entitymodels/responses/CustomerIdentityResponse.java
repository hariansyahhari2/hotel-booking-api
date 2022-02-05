package com.hariansyah.hotelbooking.api.models.entitymodels.responses;

import com.hariansyah.hotelbooking.api.enums.IdentityCategoryEnum;

public class CustomerIdentityResponse {

    private Integer id;

    private IdentityCategoryEnum identityCategory;

    private String identificationNumber;

    private String firstName;

    private String lastName;

    private String address;

    private AccountResponse account;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public IdentityCategoryEnum getIdentityCategory() {
        return identityCategory;
    }

    public void setIdentityCategory(IdentityCategoryEnum identityCategory) {
        this.identityCategory = identityCategory;
    }

    public String getIdentificationNumber() {
        return identificationNumber;
    }

    public void setIdentificationNumber(String identificationNumber) {
        this.identificationNumber = identificationNumber;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public AccountResponse getAccount() {
        return account;
    }

    public void setAccount(AccountResponse account) {
        this.account = account;
    }
}
