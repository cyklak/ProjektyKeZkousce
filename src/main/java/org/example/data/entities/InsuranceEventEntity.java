package org.example.data.entities;

import jakarta.persistence.*;

import java.util.Date;
import java.util.List;

@Entity
@Table(name = "InsuranceEvents")
public class InsuranceEventEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long insuranceEventId;

    @Column(nullable = false)
    private String nameOfEvent;

    @Column(nullable = false)
    private Date dateOfEvent;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String eventDescription;

    @ManyToMany
    @JoinTable(name = "Insurances_and_Events",
            joinColumns = @JoinColumn(name = "insurance_event_id"),
            inverseJoinColumns = @JoinColumn(name = "insurance_id"))
    private List<InsuranceEntity> insurances;
    @Column(nullable = false)
    private Long insuredId;

    @Column(nullable = false)
    private Long policyholderId;

    @Column(nullable = false)
    private String insuredFirstName;

    @Column(nullable = false)
    private String insuredLastName;

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

    public Date getDateOfEvent() {
        return dateOfEvent;
    }

    public void setDateOfEvent(Date dateOfEvent) {
        this.dateOfEvent = dateOfEvent;
    }

    public String getEventDescription() {
        return eventDescription;
    }

    public void setEventDescription(String eventDescription) {
        this.eventDescription = eventDescription;
    }

    public List<InsuranceEntity> getInsurances() {
        return insurances;
    }

    public void setInsurances(List<InsuranceEntity> insurances) {
        this.insurances = insurances;
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
}
