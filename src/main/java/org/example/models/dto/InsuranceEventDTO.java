package org.example.models.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.List;
@Getter
@Setter
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


}
