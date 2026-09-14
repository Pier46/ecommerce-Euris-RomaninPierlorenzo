package it.pierlorenzo.ecommerce.dto.prodotto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;

@Data
public class ProdottoRequest {

    @NotBlank
    private String codice;

    @NotBlank
    private String nome;

    @PositiveOrZero
    private int stock;
}