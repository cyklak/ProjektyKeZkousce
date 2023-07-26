package org.example.models.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class InsuredDTO {


    private long insuredId;
    @NotBlank(message = "Vyplňte jméno")
    @NotNull(message = "Vyplňte jméno")
    private String firstName;

    @NotBlank(message = "Vyplňte příjmení")
    @NotNull(message = "Vyplňte příjmení")
    private String lastName;

    @NotBlank(message = "Vyplňte email")
    @NotNull(message = "Vyplňte email")
    @Email(message = "Zadejte email ve správném tvaru")
    private String email;

    @NotBlank(message = "Vyplňte telefon")
    @NotNull(message = "Vyplňte telefon")
    private String phoneNumber;

    @NotBlank(message = "Vyplňte ulici a číslo popisné")
    @NotNull(message = "Vyplňte ulici a číslo popisné")
    private String street;

    @NotBlank(message = "Vyplňte město")
    @NotNull(message = "Vyplňte město")
    private String city;

    @NotBlank(message = "Vyplňte psč")
    @NotNull(message = "Vyplňte psč")
    private String zip;

    private long userId;

    private List<Long> insuranceIds;

    private long policyholderId;

}
