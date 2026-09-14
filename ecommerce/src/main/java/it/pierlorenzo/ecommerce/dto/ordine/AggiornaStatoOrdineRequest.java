package it.pierlorenzo.ecommerce.dto.ordine;

import it.pierlorenzo.ecommerce.model.StatoOrdineEnum;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AggiornaStatoOrdineRequest {

    @NotNull
    private StatoOrdineEnum status;
}