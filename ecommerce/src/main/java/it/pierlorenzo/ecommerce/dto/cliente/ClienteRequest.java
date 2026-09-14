package it.pierlorenzo.ecommerce.dto.cliente;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
public class ClienteRequest {

    @NotBlank
    private String nome;

    @NotBlank
    private String cognome;

    @NotNull
    private LocalDate dataNascita;

    @NotBlank
    private String codFiscale;

    @NotBlank
    @Email
    private String email;
}
