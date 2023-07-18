package org.example.data.entities;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "seznam_pojisteni")
public class InsuranceEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long pojisteniId;

    @Column(nullable = false)
    private String typPojisteni;

    @Column(nullable = false)
    private String castka;

    @Column(nullable = false)
    private String predmetPojisteni;

    @Column(nullable = false)
    private LocalDate platnostOd;

    @Column(nullable = false)
    private LocalDate platnostDo;

    @ManyToOne
    @JoinColumn(name = "pojistenecId")
    private InsuredEntity pojistenec;

    @ManyToMany (mappedBy = "pojisteni")
    private List<InsuranceEventEntity> seznamUdalosti;

    public long getPojisteniId() {
        return pojisteniId;
    }

    public String getTypPojisteni() {
        return typPojisteni;
    }

    public void setTypPojisteni(String typPojisteni) {
        this.typPojisteni = typPojisteni;
    }

    public void setPojisteniId(long pojisteniId) {
        this.pojisteniId = pojisteniId;
    }

    public String getCastka() {
        return castka;
    }

    public void setCastka(String castka) {
        this.castka = castka;
    }

    public String getPredmetPojisteni() {
        return predmetPojisteni;
    }

    public void setPredmetPojisteni(String predmetPojisteni) {
        this.predmetPojisteni = predmetPojisteni;
    }

    public LocalDate getPlatnostOd() {
        return platnostOd;
    }

    public void setPlatnostOd(LocalDate platnostOd) {
        this.platnostOd = platnostOd;
    }

    public LocalDate getPlatnostDo() {
        return platnostDo;
    }

    public void setPlatnostDo(LocalDate platnostDo) {
        this.platnostDo = platnostDo;
    }

    public InsuredEntity getPojistenec() {
        return pojistenec;
    }

    public void setPojistenec(InsuredEntity pojistenec) {
        this.pojistenec = pojistenec;
    }

    public List<InsuranceEventEntity> getSeznamUdalosti() {
        return seznamUdalosti;
    }

    public void setSeznamUdalosti(List<InsuranceEventEntity> seznamUdalosti) {
        this.seznamUdalosti = seznamUdalosti;
    }


}
