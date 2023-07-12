package org.example.models.dto;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;
import org.example.data.entities.PojisteniEntity;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

public class PojistnaUdalostDTO {
    private long pojistnaUdalostId;
    @NotBlank(message = "Zadejte název události")
    @NotNull(message = "Zadejte název události")
    private String nazevUdalosti;
    @PastOrPresent(message = "Nelze zvolit budoucí datum")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @NotNull(message = "Zadejte datum události")
    private LocalDate datumUdalosti;
    @NotBlank(message = "Zadejte popis události")
    @NotNull(message = "Zadejte popis události")
    private String popisUdalosti;
    @Size(min = 1, message = "Zvolte alespoň 1 aplikovatelné pojištění")
    private List<Long> pojisteniIds;

    private Long pojistenecId;

    private Long pojistnikId;

    private String jmenoPojisteneho;

    private String prijmeniPojisteneho;

    private List<Long> neplatnaPojisteni;

    public long getPojistnaUdalostId() {
        return pojistnaUdalostId;
    }

    public void setPojistnaUdalostId(long pojistnaUdalostId) {
        this.pojistnaUdalostId = pojistnaUdalostId;
    }

    public String getNazevUdalosti() {
        return nazevUdalosti;
    }

    public void setNazevUdalosti(String nazevUdalosti) {
        this.nazevUdalosti = nazevUdalosti;
    }

    public LocalDate getDatumUdalosti() {
        return datumUdalosti;
    }

    public void setDatumUdalosti(LocalDate datumUdalosti) {
        this.datumUdalosti = datumUdalosti;
    }

    public String getPopisUdalosti() {
        return popisUdalosti;
    }

    public void setPopisUdalosti(String popisUdalosti) {
        this.popisUdalosti = popisUdalosti;
    }

    public List<Long> getPojisteniIds() {
        return pojisteniIds;
    }

    public void setPojisteniIds(List<Long> pojisteniIds) {
        this.pojisteniIds = pojisteniIds;
    }

    public Long getPojistenecId() {
        return pojistenecId;
    }

    public void setPojistenecId(Long pojistenecId) {
        this.pojistenecId = pojistenecId;
    }

    public Long getPojistnikId() {
        return pojistnikId;
    }

    public void setPojistnikId(Long pojistnikId) {
        this.pojistnikId = pojistnikId;
    }

    public String getJmenoPojisteneho() {
        return jmenoPojisteneho;
    }

    public void setJmenoPojisteneho(String jmenoPojisteneho) {
        this.jmenoPojisteneho = jmenoPojisteneho;
    }

    public String getPrijmeniPojisteneho() {
        return prijmeniPojisteneho;
    }

    public void setPrijmeniPojisteneho(String prijmeniPojisteneho) {
        this.prijmeniPojisteneho = prijmeniPojisteneho;
    }

    public List<Long> getNeplatnaPojisteni() {
        return neplatnaPojisteni;
    }

    public void setNeplatnaPojisteni(List<Long> neplatnaPojisteni) {
        this.neplatnaPojisteni = neplatnaPojisteni;
    }
}
