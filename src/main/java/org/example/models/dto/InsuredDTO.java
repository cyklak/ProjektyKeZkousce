package org.example.models.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public class InsuredDTO {

    private long insuredId;
    @NotBlank(message = "Vyplňte jméno")
    @NotNull(message = "Vyplňte jméno")
    private String firstName;

    @NotBlank(message = "Vyplňte příjmení")
    @NotNull(message = "Vyplňte příjmení")
    private String lastName;

    @NotBlank(message = "Vyplňte email")
    @NotNull(message = "Vyplňte email")
    @Email(message = "Zadejte email ve správném tvaru")
    private String email;

    @NotBlank(message = "Vyplňte telefon")
    @NotNull(message = "Vyplňte telefon")
    private String phoneNumber;

    @NotBlank(message = "Vyplňte ulici a číslo popisné")
    @NotNull(message = "Vyplňte ulici a číslo popisné")
    private String street;

    @NotBlank(message = "Vyplňte město")
    @NotNull(message = "Vyplňte město")
    private String city;

    @NotBlank(message = "Vyplňte psč")
    @NotNull(message = "Vyplňte psč")
    private String zip;

    private long userId;

    private List<Long> insuranceIds;

    private long policyholderId;


    //region: Getters and setters


    public long getInsuredId() {
        return insuredId;
    }

    public void setInsuredId(long insuredId) {
        this.insuredId = insuredId;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public List<Long> getInsuranceIds() {
        return insuranceIds;
    }

    public void setInsuranceIds(List<Long> insuranceIds) {
        this.insuranceIds = insuranceIds;
    }

    public long getPolicyholderId() {
        return policyholderId;
    }

    public void setPolicyholderId(long policyholderId) {
        this.policyholderId = policyholderId;
    }
}
