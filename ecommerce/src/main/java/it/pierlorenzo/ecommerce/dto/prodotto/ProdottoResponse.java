package it.pierlorenzo.ecommerce.dto.prodotto;

import lombok.Data;

@Data
public class ProdottoResponse {

    private Long id;
    private String codice;
    private String nome;
    private int stock;
}