package org.example.data.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Entity
@Getter
@Setter
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


}
