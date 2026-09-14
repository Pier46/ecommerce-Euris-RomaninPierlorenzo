package it.pierlorenzo.ecommerce.mapper;

import it.pierlorenzo.ecommerce.dto.prodotto.ProdottoRequest;
import it.pierlorenzo.ecommerce.dto.prodotto.ProdottoResponse;
import it.pierlorenzo.ecommerce.model.prodotto.Prodotto;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ProdottoMapperTest {

    private final ProdottoMapper prodottoMapper = new ProdottoMapper();

    @Test
    void requestToEntityTest() {

        ProdottoRequest request = new ProdottoRequest();
        request.setCodice("P001");
        request.setNome("Prodotto 1");
        request.setStock(10);

        Prodotto risultato = prodottoMapper.toEntity(request);

        assertNotNull(risultato);
        assertEquals("P001", risultato.getCodice());
        assertEquals("Prodotto 1", risultato.getNome());
        assertEquals(10, risultato.getStock());
    }

    @Test
    void entitytoResponseTest() {

        Prodotto prodotto = new Prodotto();
        prodotto.setId(1L);
        prodotto.setCodice("P001");
        prodotto.setNome("Prodotto 1");
        prodotto.setStock(10);

        ProdottoResponse risultato = prodottoMapper.toResponse(prodotto);

        assertNotNull(risultato);
        assertEquals(1L, risultato.getId());
        assertEquals("P001", risultato.getCodice());
        assertEquals("Prodotto 1", risultato.getNome());
        assertEquals(10, risultato.getStock());
    }
}