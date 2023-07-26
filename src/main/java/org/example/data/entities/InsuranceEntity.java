package org.example.data.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Entity
@Getter
@Setter
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


}
