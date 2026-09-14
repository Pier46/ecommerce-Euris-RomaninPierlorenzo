package it.pierlorenzo.ecommerce.mapper;

import it.pierlorenzo.ecommerce.dto.ordine.OrdineResponse;
import it.pierlorenzo.ecommerce.model.StatoOrdineEnum;
import it.pierlorenzo.ecommerce.model.cliente.Cliente;
import it.pierlorenzo.ecommerce.model.ordine.Ordine;
import it.pierlorenzo.ecommerce.model.prodotto.Prodotto;
import it.pierlorenzo.ecommerce.model.prodottiPerOrdine.ProdottoPerOrdine;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class OrdineMapperTest {

    private final OrdineMapper ordineMapper = new OrdineMapper();

    @Test
    void entityToResponseTest() {

        Cliente cliente = new Cliente();
        cliente.setId(1L);
        cliente.setNome("Mario");
        cliente.setCognome("Rossi");

        Prodotto prodotto = new Prodotto();
        prodotto.setId(10L);
        prodotto.setCodice("P001");
        prodotto.setNome("Laptop");
        prodotto.setStock(5);

        ProdottoPerOrdine prodottoPerOrdine = new ProdottoPerOrdine();
        prodottoPerOrdine.setId(100L);
        prodottoPerOrdine.setQuantita(2);
        prodottoPerOrdine.setProdotto(prodotto);

        Ordine ordine = new Ordine();
        ordine.setId(50L);
        ordine.setCliente(cliente);
        ordine.setStatus(StatoOrdineEnum.ORDINATO);
        ordine.setProdottoPerOrdineList(List.of(prodottoPerOrdine));

        OrdineResponse risultato = ordineMapper.toResponse(ordine);

        assertNotNull(risultato);

        assertEquals(50L, risultato.getId());
        assertEquals("Mario Rossi", risultato.getCliente());
        assertEquals("ORDINATO", risultato.getStatus());

        assertNotNull(risultato.getProdotti());
        assertEquals(1, risultato.getProdotti().size());

        assertEquals("Laptop", risultato.getProdotti().get(0).getProdotto());
        assertEquals(2, risultato.getProdotti().get(0).getQuantita());
    }
}