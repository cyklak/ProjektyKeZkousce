package org.example.models.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.List;

public class PojisteniDTO {

    private long pojisteniId;
    @NotBlank(message = "Zvolte typ pojištění")
    @NotNull(message = "Zvolte typ pojištění")
    private String typPojisteni;
    @NotBlank(message = "Vyplňte částku")
    @NotNull(message = "Vyplňte částku")
    private String castka;
    @NotBlank(message = "Vyplňte předmět pojištění")
    @NotNull(message = "Vyplňte předmět pojištění")
    private String predmetPojisteni;
    @Future(message = "Nejbližší možné datum je zítřek")
    @NotNull(message = "Zvolte platnost od")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate platnostOd;
    @Future(message = "Nejbližší možné datum je zítřek")
    @NotNull(message = "Zvolte platnost do")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate platnostDo;

    private PojistenecDTO pojistenec;

    private List<PojistnaUdalostDTO> seznamUdalosti;

    private boolean aktivni;


    public long getPojisteniId() {
        return pojisteniId;
    }

    public void setPojisteniId(long pojisteniId) {
        this.pojisteniId = pojisteniId;
    }

    public String getTypPojisteni() {
        return typPojisteni;
    }

    public void setTypPojisteni(String typPojisteni) {
        this.typPojisteni = typPojisteni;
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

    public PojistenecDTO getPojistenec() {
        return pojistenec;
    }

    public void setPojistenec(PojistenecDTO pojistenec) {
        this.pojistenec = pojistenec;
    }

    public List<PojistnaUdalostDTO> getSeznamUdalosti() {
        return seznamUdalosti;
    }

    public void setSeznamUdalosti(List<PojistnaUdalostDTO> seznamUdalosti) {
        this.seznamUdalosti = seznamUdalosti;
    }

    public boolean isAktivni() {
        return aktivni;
    }

    public void setAktivni(boolean aktivni) {
        this.aktivni = aktivni;
    }
}
