package it.pierlorenzo.ecommerce.dto.prodottoPerOrdine;

import lombok.Data;

@Data
public class ProdottoPerOrdineResponse {

    private String prodotto;
    private int quantita;
}