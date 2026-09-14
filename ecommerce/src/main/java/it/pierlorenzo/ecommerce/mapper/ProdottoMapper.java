package it.pierlorenzo.ecommerce.mapper;

import it.pierlorenzo.ecommerce.dto.prodotto.ProdottoRequest;
import it.pierlorenzo.ecommerce.dto.prodotto.ProdottoResponse;
import it.pierlorenzo.ecommerce.model.prodotto.Prodotto;
import org.springframework.stereotype.Component;

@Component
public class ProdottoMapper {

    public Prodotto toEntity(ProdottoRequest request) {

        Prodotto prodotto = new Prodotto();

        prodotto.setCodice(request.getCodice());
        prodotto.setNome(request.getNome());
        prodotto.setStock(request.getStock());

        return prodotto;
    }

    public ProdottoResponse toResponse(Prodotto prodotto) {

        ProdottoResponse response = new ProdottoResponse();

        response.setId(prodotto.getId());
        response.setCodice(prodotto.getCodice());
        response.setNome(prodotto.getNome());
        response.setStock(prodotto.getStock());

        return response;
    }
}