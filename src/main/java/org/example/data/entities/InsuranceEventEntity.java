package org.example.data.entities;

import jakarta.persistence.*;

import java.util.Date;
import java.util.List;

@Entity
@Table(name = "PojistneUdalosti")
public class InsuranceEventEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long pojistnaUdalostId;

    @Column(nullable = false)
    private String NazevUdalosti;

    @Column(nullable = false)
    private Date datumUdalosti;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String popisUdalosti;

    @ManyToMany
    @JoinTable(name = "Pojisteni_a_Udalosti",
            joinColumns = @JoinColumn(name = "pojistna_udalost_id"),
            inverseJoinColumns = @JoinColumn(name = "pojisteni_id"))
    private List<InsuranceEntity> pojisteni;
    @Column(nullable = false)
    private Long pojistenecId;

    @Column(nullable = false)
    private Long pojistnikId;

    @Column(nullable = false)
    private String jmenoPojisteneho;

    @Column(nullable = false)
    private String prijmeniPojisteneho;


    public long getPojistnaUdalostId() {
        return pojistnaUdalostId;
    }

    public void setPojistnaUdalostId(long pojistnaUdalostId) {
        this.pojistnaUdalostId = pojistnaUdalostId;
    }

    public String getNazevUdalosti() {
        return NazevUdalosti;
    }

    public void setNazevUdalosti(String nazevUdalosti) {
        NazevUdalosti = nazevUdalosti;
    }

    public Date getDatumUdalosti() {
        return datumUdalosti;
    }

    public void setDatumUdalosti(Date datumUdalosti) {
        this.datumUdalosti = datumUdalosti;
    }

    public String getPopisUdalosti() {
        return popisUdalosti;
    }

    public void setPopisUdalosti(String popisUdalosti) {
        this.popisUdalosti = popisUdalosti;
    }

    public List<InsuranceEntity> getPojisteni() {
        return pojisteni;
    }

    public void setPojisteni(List<InsuranceEntity> pojisteni) {
        this.pojisteni = pojisteni;
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
}
