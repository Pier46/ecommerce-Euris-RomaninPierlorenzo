package it.pierlorenzo.ecommerce.model;

import it.pierlorenzo.ecommerce.model.cliente.Cliente;
import it.pierlorenzo.ecommerce.model.ordine.Ordine;
import it.pierlorenzo.ecommerce.model.prodotto.Prodotto;
import it.pierlorenzo.ecommerce.model.prodottiPerOrdine.ProdottoPerOrdine;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class EntityTest {

    @Test
    void clienteConstructorTest() {

        Cliente cliente = new Cliente();

        cliente.setId(1L);
        cliente.setNome("Mario");
        cliente.setCognome("Rossi");
        cliente.setCodFiscale("RSSMRA80A01H501X");
        cliente.setEmail("mario.rossi@test.it");

        LocalDate dataNascita = LocalDate.of(1980, 1, 1);
        cliente.setDataNascita(dataNascita);

        assertEquals(1L, cliente.getId());
        assertEquals("Mario", cliente.getNome());
        assertEquals("Rossi", cliente.getCognome());
        assertEquals("RSSMRA80A01H501X", cliente.getCodFiscale());
        assertEquals("mario.rossi@test.it", cliente.getEmail());
        assertEquals(dataNascita, cliente.getDataNascita());
    }

    @Test
    void prodottoConstructorTest() {

        Prodotto prodotto = new Prodotto();

        prodotto.setId(1L);
        prodotto.setCodice("P001");
        prodotto.setNome("Laptop");
        prodotto.setStock(10);

        assertEquals(1L, prodotto.getId());
        assertEquals("P001", prodotto.getCodice());
        assertEquals("Laptop", prodotto.getNome());
        assertEquals(10, prodotto.getStock());

        assertNull(prodotto.getVersion());
    }

    @Test
    void prodottoPerOrdineConstructorTest() {

        Prodotto prodotto = new Prodotto();
        prodotto.setNome("Laptop");

        Ordine ordine = new Ordine();

        ProdottoPerOrdine prodottoPerOrdine = new ProdottoPerOrdine();

        prodottoPerOrdine.setId(1L);
        prodottoPerOrdine.setQuantita(3);
        prodottoPerOrdine.setProdotto(prodotto);
        prodottoPerOrdine.setOrdine(ordine);

        assertEquals(1L, prodottoPerOrdine.getId());
        assertEquals(3, prodottoPerOrdine.getQuantita());
        assertEquals(prodotto, prodottoPerOrdine.getProdotto());
        assertEquals(ordine, prodottoPerOrdine.getOrdine());
    }

    @Test
    void ordineConstructorTest() {

        Cliente cliente = new Cliente();
        cliente.setNome("Mario");

        ProdottoPerOrdine prodottoPerOrdine = new ProdottoPerOrdine();
        prodottoPerOrdine.setQuantita(2);

        Ordine ordine = new Ordine();

        ordine.setId(1L);
        ordine.setCliente(cliente);
        ordine.setStatus(StatoOrdineEnum.ORDINATO);
        ordine.setProdottoPerOrdineList(
                new ArrayList<>(java.util.List.of(prodottoPerOrdine))
        );

        assertEquals(1L, ordine.getId());
        assertEquals(cliente, ordine.getCliente());
        assertEquals(StatoOrdineEnum.ORDINATO, ordine.getStatus());
        assertEquals(1, ordine.getProdottoPerOrdineList().size());
        assertEquals(prodottoPerOrdine, ordine.getProdottoPerOrdineList().get(0));
    }
}