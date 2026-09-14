package it.pierlorenzo.ecommerce.dto.ordine;

import it.pierlorenzo.ecommerce.dto.prodottoPerOrdine.ProdottoPerOrdineRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class OrdineRequest {

    @NotNull
    private Long clienteId;

    @NotEmpty
    @Valid
    private List<ProdottoPerOrdineRequest> prodotti;

    // getter e setter
}
