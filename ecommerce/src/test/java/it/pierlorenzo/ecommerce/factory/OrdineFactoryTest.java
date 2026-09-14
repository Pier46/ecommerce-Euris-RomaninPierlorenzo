package it.pierlorenzo.ecommerce.factory;

import it.pierlorenzo.ecommerce.model.StatoOrdineEnum;
import it.pierlorenzo.ecommerce.model.cliente.Cliente;
import it.pierlorenzo.ecommerce.model.ordine.Ordine;
import it.pierlorenzo.ecommerce.model.prodottiPerOrdine.ProdottoPerOrdine;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class OrdineFactoryTest {

    private final OrdineFactory ordineFactory = new OrdineFactory();

    @Test
    void creaOrdineTest() {

        Cliente cliente = new Cliente();
        cliente.setId(1L);
        cliente.setNome("Mario");
        cliente.setCognome("Rossi");

        ProdottoPerOrdine prodotto1 = new ProdottoPerOrdine();
        prodotto1.setQuantita(2);

        ProdottoPerOrdine prodotto2 = new ProdottoPerOrdine();
        prodotto2.setQuantita(3);

        List<ProdottoPerOrdine> prodotti = List.of(prodotto1, prodotto2);

        Ordine ordine = ordineFactory.create(cliente, prodotti);

        assertNotNull(ordine);

        assertEquals(cliente, ordine.getCliente());
        assertEquals(StatoOrdineEnum.ORDINATO, ordine.getStatus());

        assertNotNull(ordine.getProdottoPerOrdineList());
        assertEquals(2, ordine.getProdottoPerOrdineList().size());

        assertEquals(prodotto1, ordine.getProdottoPerOrdineList().get(0));
        assertEquals(prodotto2, ordine.getProdottoPerOrdineList().get(1));

        assertEquals(ordine, prodotto1.getOrdine());
        assertEquals(ordine, prodotto2.getOrdine());
    }
}