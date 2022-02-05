package com.hariansyah.hotelbooking.api.models.entitymodels.requests;

import com.hariansyah.hotelbooking.api.enums.IdentityCategoryEnum;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class CustomerIdentityWithAccountRequest {

    @NotNull
    private IdentityCategoryEnum identityCategory;

    @NotBlank
    private String identificationNumber;

    @NotBlank
    private String firstName;

    @NotBlank
    private String lastName;

    @NotBlank
    private String address;

    @NotNull
    private AccountRequest account;

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

    public AccountRequest getAccount() {
        return account;
    }

    public void setAccount(AccountRequest account) {
        this.account = account;
    }
}
