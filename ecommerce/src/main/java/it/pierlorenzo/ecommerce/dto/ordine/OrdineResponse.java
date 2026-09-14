package it.pierlorenzo.ecommerce.dto.ordine;

import it.pierlorenzo.ecommerce.dto.prodottoPerOrdine.ProdottoPerOrdineResponse;
import lombok.Data;

import java.util.List;

@Data
public class OrdineResponse {

    private Long id;
    private String cliente;
    private List<ProdottoPerOrdineResponse> prodotti;
    private String status;
}
