package org.example.data.entities;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "insurance_list")
public class InsuranceEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long insuranceId;

    @Column(nullable = false)
    private String insuranceType;

    @Column(nullable = false)
    private String amount;

    @Column(nullable = false)
    private String insuredObject;

    @Column(nullable = false)
    private LocalDate validFrom;

    @Column(nullable = false)
    private LocalDate validUntil;

    @ManyToOne
    @JoinColumn(name = "insuredId")
    private InsuredEntity insured;

    @ManyToMany (mappedBy = "insurances")
    private List<InsuranceEventEntity> eventList;

    public long getInsuranceId() {
        return insuranceId;
    }

    public void setInsuranceId(long insuranceId) {
        this.insuranceId = insuranceId;
    }

    public String getInsuranceType() {
        return insuranceType;
    }

    public void setInsuranceType(String insuranceType) {
        this.insuranceType = insuranceType;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getInsuredObject() {
        return insuredObject;
    }

    public void setInsuredObject(String insuredObject) {
        this.insuredObject = insuredObject;
    }

    public LocalDate getValidFrom() {
        return validFrom;
    }

    public void setValidFrom(LocalDate validFrom) {
        this.validFrom = validFrom;
    }

    public LocalDate getValidUntil() {
        return validUntil;
    }

    public void setValidUntil(LocalDate validUntil) {
        this.validUntil = validUntil;
    }

    public InsuredEntity getInsured() {
        return insured;
    }

    public void setInsured(InsuredEntity insured) {
        this.insured = insured;
    }

    public List<InsuranceEventEntity> getEventList() {
        return eventList;
    }

    public void setEventList(List<InsuranceEventEntity> eventList) {
        this.eventList = eventList;
    }
}
