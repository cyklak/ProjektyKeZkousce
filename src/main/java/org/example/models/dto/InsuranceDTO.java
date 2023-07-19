package org.example.models.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.List;

public class InsuranceDTO {

    private long insuranceId;
    @NotBlank(message = "Zvolte typ pojištění")
    @NotNull(message = "Zvolte typ pojištění")
    private String insuranceType;
    @NotBlank(message = "Vyplňte částku")
    @NotNull(message = "Vyplňte částku")
    private String amount;
    @NotBlank(message = "Vyplňte předmět pojištění")
    @NotNull(message = "Vyplňte předmět pojištění")
    private String insuredObject;
    @Future(message = "Nejbližší možné datum je zítřek")
    @NotNull(message = "Zvolte platnost od")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate validFrom;
    @Future(message = "Nejbližší možné datum je zítřek")
    @NotNull(message = "Zvolte platnost do")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate validUntil;

    private InsuredDTO insured;

    private List<InsuranceEventDTO> eventList;

    private boolean active;

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

    public InsuredDTO getInsured() {
        return insured;
    }

    public void setInsured(InsuredDTO insured) {
        this.insured = insured;
    }

    public List<InsuranceEventDTO> getEventList() {
        return eventList;
    }

    public void setEventList(List<InsuranceEventDTO> eventList) {
        this.eventList = eventList;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
