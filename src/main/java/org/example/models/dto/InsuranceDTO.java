package org.example.models.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.List;
@Getter
@Setter
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


}
