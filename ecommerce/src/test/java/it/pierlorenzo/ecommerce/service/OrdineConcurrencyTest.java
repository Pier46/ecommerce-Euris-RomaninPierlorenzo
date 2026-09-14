package it.pierlorenzo.ecommerce.service;

import it.pierlorenzo.ecommerce.dto.ordine.OrdineRequest;
import it.pierlorenzo.ecommerce.dto.prodottoPerOrdine.ProdottoPerOrdineRequest;
import it.pierlorenzo.ecommerce.exception.StockInsufficienteException;
import it.pierlorenzo.ecommerce.model.cliente.Cliente;
import it.pierlorenzo.ecommerce.model.prodotto.Prodotto;
import it.pierlorenzo.ecommerce.repository.ClienteRepository;
import it.pierlorenzo.ecommerce.repository.OrdineRepository;
import it.pierlorenzo.ecommerce.repository.ProdottoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.List;
import java.util.concurrent.*;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class OrdineConcurrencyTest {

    @Autowired
    private OrdineService ordineService;

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private ProdottoRepository prodottoRepository;

    @Autowired
    private OrdineRepository ordineRepository;

    @Autowired
    private PlatformTransactionManager transactionManager;

    private Cliente cliente;
    private Prodotto prodotto;

    @BeforeEach
    void setUp() {
        ordineRepository.deleteAll();
        prodottoRepository.deleteAll();
        clienteRepository.deleteAll();

        cliente = new Cliente();
        cliente.setNome("Mario");
        cliente.setCognome("Rossi");
        cliente.setCodFiscale("RSSMRA80A01H501Z");
        cliente.setEmail("mario.rossi@test.it");

        cliente = clienteRepository.save(cliente);

        prodotto = new Prodotto();
        prodotto.setCodice("P001");
        prodotto.setNome("Prodotto test");
        prodotto.setStock(10);

        prodotto = prodottoRepository.save(prodotto);
    }

    @Test
    void ordiniContemporaneiNonDevonoSuperareLoStock() throws Exception {

        OrdineRequest ordineA = creaOrdineRequest(7);
        OrdineRequest ordineB = creaOrdineRequest(5);

        ExecutorService executor = Executors.newFixedThreadPool(2);

        CyclicBarrier barrier = new CyclicBarrier(2);

        Callable<Boolean> taskA = () -> {
            barrier.await();

            try {
                ordineService.creaOrdine(ordineA);
                return true;
            } catch (StockInsufficienteException e) {
                return false;
            }
        };

        Callable<Boolean> taskB = () -> {
            barrier.await();

            try {
                ordineService.creaOrdine(ordineB);
                return true;
            } catch (StockInsufficienteException e) {
                return false;
            }
        };

        List<Future<Boolean>> risultati = executor.invokeAll(
                List.of(taskA, taskB)
        );

        executor.shutdown();

        assertTrue(executor.awaitTermination(5, TimeUnit.SECONDS));

        long ordiniCreati = risultati.stream()
                .filter(futuro -> {
                    try {
                        return futuro.get();
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                })
                .count();

        Prodotto prodottoFinale = prodottoRepository
                .findById(prodotto.getId())
                .orElseThrow();

        assertEquals(1, ordiniCreati);
        assertTrue(
                prodottoFinale.getStock() == 3
                        || prodottoFinale.getStock() == 5
        );
        assertTrue(prodottoFinale.getStock() >= 0);
        assertEquals(1, ordineRepository.count());
    }

    @Test
    void deveRilevareConflittoDiOptimisticLocking() {

        TransactionTemplate transactionTemplate =
                new TransactionTemplate(transactionManager);

        Prodotto prodottoA = transactionTemplate.execute(status ->
                prodottoRepository.findById(prodotto.getId())
                        .orElseThrow()
        );

        Prodotto prodottoB = transactionTemplate.execute(status ->
                prodottoRepository.findById(prodotto.getId())
                        .orElseThrow()
        );

        assertNotNull(prodottoA);
        assertNotNull(prodottoB);

        assertEquals(
                prodottoA.getVersion(),
                prodottoB.getVersion()
        );

        prodottoA.setStock(7);

        transactionTemplate.executeWithoutResult(status ->
                prodottoRepository.saveAndFlush(prodottoA)
        );

        Prodotto prodottoDopoPrimaModifica =
                prodottoRepository.findById(prodotto.getId())
                        .orElseThrow();

        assertEquals(7, prodottoDopoPrimaModifica.getStock());

        assertThrows(
                ObjectOptimisticLockingFailureException.class,
                () -> transactionTemplate.executeWithoutResult(status -> {
                    prodottoB.setStock(5);
                    prodottoRepository.saveAndFlush(prodottoB);
                })
        );
    }

    private OrdineRequest creaOrdineRequest(int quantita) {

        ProdottoPerOrdineRequest prodottoRequest =
                new ProdottoPerOrdineRequest();

        prodottoRequest.setProdottoId(prodotto.getId());
        prodottoRequest.setQuantita(quantita);

        OrdineRequest ordineRequest = new OrdineRequest();

        ordineRequest.setClienteId(cliente.getId());
        ordineRequest.setProdotti(List.of(prodottoRequest));

        return ordineRequest;
    }
}