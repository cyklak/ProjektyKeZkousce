package org.example.models.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDTO {

    private long userId;

    @Email(message = "Vyplňte validní email")
    @NotBlank(message = "Vyplňte uživatelský email")
    @NotNull(message = "Vyplňte uživatelský email")
    private String email;

    @NotBlank(message = "Vyplňte uživatelské heslo")
    @NotNull(message = "Vyplňte uživatelské heslo")
    @Size(min = 6, message = "Heslo musí mít alespoň 6 znaků")
    private String password;

    @NotBlank(message = "Vyplňte uživatelské heslo")
    @NotNull(message = "Vyplňte uživatelské heslo")
    @Size(min = 6, message = "Heslo musí mít alespoň 6 znaků")
    private String confirmPassword;

}