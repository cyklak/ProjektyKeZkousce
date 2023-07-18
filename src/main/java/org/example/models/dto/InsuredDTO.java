package org.example.models.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public class InsuredDTO {

    private long pojistenecId;
    @NotBlank(message = "Vyplňte jméno")
    @NotNull(message = "Vyplňte jméno")
    private String jmeno;

    @NotBlank(message = "Vyplňte příjmení")
    @NotNull(message = "Vyplňte příjmení")
    private String prijmeni;

    @NotBlank(message = "Vyplňte email")
    @NotNull(message = "Vyplňte email")
    @Email(message = "Zadejte email ve správném tvaru")
    private String email;

    @NotBlank(message = "Vyplňte telefon")
    @NotNull(message = "Vyplňte telefon")
    private String telefon;

    @NotBlank(message = "Vyplňte ulici a číslo popisné")
    @NotNull(message = "Vyplňte ulici a číslo popisné")
    private String ulice;

    @NotBlank(message = "Vyplňte město")
    @NotNull(message = "Vyplňte město")
    private String mesto;

    @NotBlank(message = "Vyplňte psč")
    @NotNull(message = "Vyplňte psč")
    private String psc;

    private long userId;

    private List<Long> pojisteniIds;

    private long pojistnikId;


    //region: Getters and setters

    public String getJmeno() {
        return jmeno;
    }

    public void setJmeno(String jmeno) {
        this.jmeno = jmeno;
    }

    public String getPrijmeni() {
        return prijmeni;
    }

    public void setPrijmeni(String prijmeni) {
        this.prijmeni = prijmeni;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefon() {
        return telefon;
    }

    public void setTelefon(String telefon) {
        this.telefon = telefon;
    }

    public String getUlice() {
        return ulice;
    }

    public void setUlice(String ulice) {
        this.ulice = ulice;
    }

    public String getMesto() {
        return mesto;
    }

    public void setMesto(String mesto) {
        this.mesto = mesto;
    }

    public String getPsc() {
        return psc;
    }

    public void setPsc(String psc) {
        this.psc = psc;
    }

    public long getPojistenecId() {
        return pojistenecId;
    }

    public void setPojistenecId(long pojistenecId) {
        this.pojistenecId = pojistenecId;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public List<Long> getPojisteniIds() {
        return pojisteniIds;
    }

    public void setPojisteniIds(List<Long> pojisteniIds) {
        this.pojisteniIds = pojisteniIds;
    }

    public long getPojistnikId() {
        return pojistnikId;
    }

    public void setPojistnikId(long pojistnikId) {
        this.pojistnikId = pojistnikId;
    }
}
