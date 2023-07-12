package org.example.data.entities;

import jakarta.persistence.*;
import org.springframework.beans.factory.annotation.Value;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "seznam_pojisteni")
public class PojisteniEntity {
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
    private PojistenecEntity pojistenec;

    @ManyToMany (mappedBy = "pojisteni")
    private List<PojistnaUdalostEntity> seznamUdalosti;

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

    public PojistenecEntity getPojistenec() {
        return pojistenec;
    }

    public void setPojistenec(PojistenecEntity pojistenec) {
        this.pojistenec = pojistenec;
    }

    public List<PojistnaUdalostEntity> getSeznamUdalosti() {
        return seznamUdalosti;
    }

    public void setSeznamUdalosti(List<PojistnaUdalostEntity> seznamUdalosti) {
        this.seznamUdalosti = seznamUdalosti;
    }


}
