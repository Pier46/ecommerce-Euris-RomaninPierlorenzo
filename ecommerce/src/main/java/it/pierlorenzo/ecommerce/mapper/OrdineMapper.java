package it.pierlorenzo.ecommerce.mapper;



import it.pierlorenzo.ecommerce.model.ordine.Ordine;
import it.pierlorenzo.ecommerce.dto.ordine.OrdineResponse;
import it.pierlorenzo.ecommerce.model.prodottiPerOrdine.ProdottoPerOrdine;
import it.pierlorenzo.ecommerce.dto.prodottoPerOrdine.ProdottoPerOrdineResponse;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class OrdineMapper {

    public OrdineResponse toResponse(Ordine ordine) {

        OrdineResponse response = new OrdineResponse();

        response.setId(ordine.getId());

        response.setCliente(
                ordine.getCliente().getNome()
                        + " "
                        + ordine.getCliente().getCognome()
        );

        response.setStatus(ordine.getStatus().toString());

        List<ProdottoPerOrdineResponse> prodotti =
                ordine.getProdottoPerOrdineList()
                        .stream()
                        .map(this::toProdottoResponse)
                        .toList();

        response.setProdotti(prodotti);

        return response;
    }

    private ProdottoPerOrdineResponse toProdottoResponse(
            ProdottoPerOrdine prodottoPerOrdine) {

        ProdottoPerOrdineResponse response =
                new ProdottoPerOrdineResponse();

        response.setProdotto(
                prodottoPerOrdine.getProdotto().getNome()
        );

        response.setQuantita(
                prodottoPerOrdine.getQuantita()
        );

        return response;
    }
}
