package org.example.models.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.List;

public class InsuranceEventDTO {
    private long insuranceEventId;
    @NotBlank(message = "Zadejte název události")
    @NotNull(message = "Zadejte název události")
    private String nameOfEvent;
    @PastOrPresent(message = "Nelze zvolit budoucí datum")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @NotNull(message = "Zadejte datum události")
    private LocalDate dateOfEvent;
    @NotBlank(message = "Zadejte popis události")
    @NotNull(message = "Zadejte popis události")
    private String eventDescription;
    @Size(min = 1, message = "Zvolte alespoň 1 aplikovatelné pojištění")
    private List<Long> insuranceIds;

    private Long insuredId;

    private Long policyholderId;

    private String insuredFirstName;

    private String insuredLastName;

    private List<Long> invalidInsurances;

    public long getInsuranceEventId() {
        return insuranceEventId;
    }

    public void setInsuranceEventId(long insuranceEventId) {
        this.insuranceEventId = insuranceEventId;
    }

    public String getNameOfEvent() {
        return nameOfEvent;
    }

    public void setNameOfEvent(String nameOfEvent) {
        this.nameOfEvent = nameOfEvent;
    }

    public LocalDate getDateOfEvent() {
        return dateOfEvent;
    }

    public void setDateOfEvent(LocalDate dateOfEvent) {
        this.dateOfEvent = dateOfEvent;
    }

    public String getEventDescription() {
        return eventDescription;
    }

    public void setEventDescription(String eventDescription) {
        this.eventDescription = eventDescription;
    }

    public List<Long> getInsuranceIds() {
        return insuranceIds;
    }

    public void setInsuranceIds(List<Long> insuranceIds) {
        this.insuranceIds = insuranceIds;
    }

    public Long getInsuredId() {
        return insuredId;
    }

    public void setInsuredId(Long insuredId) {
        this.insuredId = insuredId;
    }

    public Long getPolicyholderId() {
        return policyholderId;
    }

    public void setPolicyholderId(Long policyholderId) {
        this.policyholderId = policyholderId;
    }

    public String getInsuredFirstName() {
        return insuredFirstName;
    }

    public void setInsuredFirstName(String insuredFirstName) {
        this.insuredFirstName = insuredFirstName;
    }

    public String getInsuredLastName() {
        return insuredLastName;
    }

    public void setInsuredLastName(String insuredLastName) {
        this.insuredLastName = insuredLastName;
    }

    public List<Long> getInvalidInsurances() {
        return invalidInsurances;
    }

    public void setInvalidInsurances(List<Long> invalidInsurances) {
        this.invalidInsurances = invalidInsurances;
    }
}
