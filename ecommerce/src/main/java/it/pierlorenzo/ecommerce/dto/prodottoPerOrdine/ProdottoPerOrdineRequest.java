package it.pierlorenzo.ecommerce.dto.prodottoPerOrdine;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class ProdottoPerOrdineRequest {

    @NotNull
    private Long prodottoId;

    @Positive
    private int quantita;

}
