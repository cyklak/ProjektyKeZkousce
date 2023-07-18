package org.example.data.entities;
import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "seznam_pojistencu")
public class InsuredEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long pojistenecId;

    @Column(nullable = false)
    private String jmeno;

    @Column(nullable = false)
    private String prijmeni;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String telefon;

    @Column(nullable = false)
    private String ulice;

    @Column(nullable = false)
    private String mesto;

    @Column(nullable = false)
    private String psc;

    @OneToMany(mappedBy = "pojistenec")
    private List<InsuranceEntity> seznamPojisteni;

    @OneToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;
    @Column(nullable = false)
    private long pojistnikId;


    public long getPojistenecId() {
        return pojistenecId;
    }

    public void setPojistenecId(long pojistenecId) {
        this.pojistenecId = pojistenecId;
    }

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

    public List<InsuranceEntity> getSeznamPojisteni() {
        return seznamPojisteni;
    }

    public void setSeznamPojisteni(List<InsuranceEntity> seznamPojisteni) {
        this.seznamPojisteni = seznamPojisteni;
    }

    public UserEntity getUserEntity() {
        return user;
    }

    public void setUserEntity(UserEntity userEntity) {
        this.user = userEntity;
    }

    public long getPojistnikId() {
        return pojistnikId;
    }

    public void setPojistnikId(long pojistnikId) {
        this.pojistnikId = pojistnikId;
    }
}
