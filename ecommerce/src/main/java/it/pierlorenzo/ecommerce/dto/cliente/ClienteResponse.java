package it.pierlorenzo.ecommerce.dto.cliente;

import lombok.Data;

import java.time.LocalDate;

@Data
public class ClienteResponse {

    private Long id;
    private String nome;
    private String cognome;
    private LocalDate dataNascita;
    private String codFiscale;
    private String email;
}