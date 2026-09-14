package it.pierlorenzo.ecommerce.service;

import it.pierlorenzo.ecommerce.dto.ordine.AggiornaStatoOrdineRequest;
import it.pierlorenzo.ecommerce.dto.ordine.CancellaOrdiniRequest;
import it.pierlorenzo.ecommerce.dto.ordine.OrdineRequest;
import it.pierlorenzo.ecommerce.dto.ordine.OrdineResponse;
import it.pierlorenzo.ecommerce.dto.prodottoPerOrdine.ProdottoPerOrdineRequest;
import it.pierlorenzo.ecommerce.exception.*;
import it.pierlorenzo.ecommerce.model.StatoOrdineEnum;
import it.pierlorenzo.ecommerce.model.cliente.Cliente;
import it.pierlorenzo.ecommerce.model.ordine.Ordine;
import it.pierlorenzo.ecommerce.model.prodottiPerOrdine.ProdottoPerOrdine;
import it.pierlorenzo.ecommerce.model.prodotto.Prodotto;
import it.pierlorenzo.ecommerce.repository.ClienteRepository;
import it.pierlorenzo.ecommerce.repository.OrdineRepository;
import it.pierlorenzo.ecommerce.repository.ProdottoRepository;
import it.pierlorenzo.ecommerce.factory.OrdineFactory;
import it.pierlorenzo.ecommerce.mapper.OrdineMapper;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrdineServiceTest {

    @Mock
    private ClienteRepository clienteRepository;

    @Mock
    private ProdottoRepository prodottoRepository;

    @Mock
    private OrdineRepository ordineRepository;

    @Mock
    private OrdineFactory ordineFactory;

    @Mock
    private OrdineMapper ordineMapper;

    @InjectMocks
    private OrdineService ordineService;

    @Test
    public void creaOrdinteTestSuccess() {
        Cliente cliente = new Cliente();
        cliente.setId(1L);
        cliente.setNome("Mario");
        cliente.setCognome("Rossi");

        Prodotto prodotto = new Prodotto();
        prodotto.setId(1L);
        prodotto.setCodice("P001");
        prodotto.setNome("Prodotto test");
        prodotto.setStock(10);

        when(clienteRepository.findById(1L))
                .thenReturn(Optional.of(cliente));

        when(prodottoRepository.findById(1L))
                .thenReturn(Optional.of(prodotto));

        OrdineRequest request = new OrdineRequest();
        request.setClienteId(1L);

        ProdottoPerOrdineRequest prodottoRequest =
                new ProdottoPerOrdineRequest();

        prodottoRequest.setProdottoId(1L);
        prodottoRequest.setQuantita(3);

        request.setProdotti(List.of(prodottoRequest));

        ProdottoPerOrdine prodottoPerOrdine = new ProdottoPerOrdine();
        prodottoPerOrdine.setProdotto(prodotto);
        prodottoPerOrdine.setQuantita(3);

        List<ProdottoPerOrdine> prodottiPerOrdine =
                List.of(prodottoPerOrdine);

        Ordine ordine = new Ordine();
        ordine.setId(1L);
        ordine.setCliente(cliente);
        ordine.setStatus(StatoOrdineEnum.ORDINATO);
        ordine.setProdottoPerOrdineList(prodottiPerOrdine);

        when(ordineFactory.create(cliente, prodottiPerOrdine))
                .thenReturn(ordine);
        when(ordineRepository.save(ordine))
                .thenReturn(ordine);

        OrdineResponse response = new OrdineResponse();
        response.setId(1L);
        response.setCliente("Mario Rossi");
        response.setStatus("ORDINATO");
        response.setProdotti(List.of());

        when(ordineMapper.toResponse(any(Ordine.class)))
                .thenReturn(response);

        OrdineResponse risultato = ordineService.creaOrdine(request);

        assertEquals(1L, risultato.getId());
        assertEquals("Mario Rossi", risultato.getCliente());
        assertEquals("ORDINATO", risultato.getStatus());
        assertEquals(7, prodotto.getStock());

        verify(clienteRepository).findById(1L);
        verify(prodottoRepository).findById(1L);
        verify(ordineRepository).save(ordine);
    }

    @Test
    public void creaOrdineNoCLiente() {

        OrdineRequest request = new OrdineRequest();
        request.setClienteId(1L);

        ProdottoPerOrdineRequest prodottoRequest =
                new ProdottoPerOrdineRequest();

        prodottoRequest.setProdottoId(1L);
        prodottoRequest.setQuantita(3);

        request.setProdotti(List.of(prodottoRequest));

        when(clienteRepository.findById(1L))
                .thenReturn(Optional.empty());

        assertThrows(
                ClienteNonTrovatoException.class,
                () -> ordineService.creaOrdine(request)
        );
    }

    @Test
    void creaOrdineProdottoNonTrovato() {
        OrdineRequest request = new OrdineRequest();
        request.setClienteId(1L);

        ProdottoPerOrdineRequest prodottoRequest =
                new ProdottoPerOrdineRequest();

        prodottoRequest.setProdottoId(999L);
        prodottoRequest.setQuantita(3);

        request.setProdotti(List.of(prodottoRequest));

        Cliente cliente = new Cliente();
        cliente.setId(1L);

        when(clienteRepository.findById(1L))
                .thenReturn(Optional.of(cliente));

        when(prodottoRepository.findById(999L))
                .thenReturn(Optional.empty());

        assertThrows(
                ProdottoNonTrovatoException.class,
                () -> ordineService.creaOrdine(request)
        );
    }

    @Test
    void creaOrdineStockInsufficiente() {
        Cliente cliente = new Cliente();
        cliente.setId(1L);

        Prodotto prodotto = new Prodotto();
        prodotto.setId(1L);
        prodotto.setCodice("P001");
        prodotto.setNome("Prodotto test");
        prodotto.setStock(10);

        OrdineRequest request = new OrdineRequest();
        request.setClienteId(1L);

        ProdottoPerOrdineRequest prodottoRequest =
                new ProdottoPerOrdineRequest();

        prodottoRequest.setProdottoId(1L);
        prodottoRequest.setQuantita(11);

        request.setProdotti(List.of(prodottoRequest));

        when(clienteRepository.findById(1L))
                .thenReturn(Optional.of(cliente));

        when(prodottoRepository.findById(1L))
                .thenReturn(Optional.of(prodotto));

        assertThrows(
                StockInsufficienteException.class,
                () -> ordineService.creaOrdine(request)
        );

        assertEquals(10, prodotto.getStock());
        verify(ordineRepository, never()).save(any(Ordine.class));
    }

    @Test
    void aggiornaStatoOrdineCorrettamente() {
        Ordine ordine = new Ordine();
        ordine.setId(1L);
        ordine.setStatus(StatoOrdineEnum.ORDINATO);

        AggiornaStatoOrdineRequest request =
                new AggiornaStatoOrdineRequest();
        request.setStatus(StatoOrdineEnum.CONSEGNATO);

        OrdineResponse response = new OrdineResponse();
        response.setId(1L);
        response.setStatus("CONSEGNATO");

        when(ordineRepository.findById(1L))
                .thenReturn(Optional.of(ordine));

        when(ordineRepository.save(ordine))
                .thenReturn(ordine);

        when(ordineMapper.toResponse(ordine))
                .thenReturn(response);

        OrdineResponse risultato =
                ordineService.aggiornaStato(1L, request);

        assertEquals(
                StatoOrdineEnum.CONSEGNATO,
                ordine.getStatus()
        );

        assertEquals(
                "CONSEGNATO",
                risultato.getStatus()
        );

        verify(ordineRepository).save(ordine);
    }

    @Test
    void nonDeveRiportareOrdineConsegnatoAOrdinato() {
        Ordine ordine = new Ordine();
        ordine.setId(1L);
        ordine.setStatus(StatoOrdineEnum.CONSEGNATO);

        AggiornaStatoOrdineRequest request =
                new AggiornaStatoOrdineRequest();
        request.setStatus(StatoOrdineEnum.ORDINATO);

        when(ordineRepository.findById(1L))
                .thenReturn(Optional.of(ordine));

        assertThrows(
                StatoOrdineNonValidoException.class,
                () -> ordineService.aggiornaStato(1L, request)
        );

        verify(ordineRepository, never()).save(any(Ordine.class));

        assertEquals(
                StatoOrdineEnum.CONSEGNATO,
                ordine.getStatus()
        );
    }

    @Test
    void cancellaOrdineERipristinaStock() {
        Prodotto prodotto = new Prodotto();
        prodotto.setId(1L);
        prodotto.setCodice("P001");
        prodotto.setNome("Prodotto test");
        prodotto.setStock(7);

        Ordine ordine = new Ordine();
        ordine.setId(1L);
        ordine.setStatus(StatoOrdineEnum.ORDINATO);

        ProdottoPerOrdine prodottoPerOrdine =
                new ProdottoPerOrdine();

        prodottoPerOrdine.setId(1L);
        prodottoPerOrdine.setProdotto(prodotto);
        prodottoPerOrdine.setQuantita(3);
        prodottoPerOrdine.setOrdine(ordine);

        ordine.setProdottoPerOrdineList(
                List.of(prodottoPerOrdine)
        );

        CancellaOrdiniRequest request =
                new CancellaOrdiniRequest();

        request.setOrdineIds(List.of(1L));

        when(ordineRepository.findAllById(List.of(1L)))
                .thenReturn(List.of(ordine));

        ordineService.cancellaOrdini(request);

        assertEquals(10, prodotto.getStock());

        verify(ordineRepository).deleteAll(List.of(ordine));
    }

    @Test
    void nonDeveCancellareOrdineConsegnato() {
        Ordine ordine = new Ordine();
        ordine.setId(1L);
        ordine.setStatus(StatoOrdineEnum.CONSEGNATO);

        CancellaOrdiniRequest request =
                new CancellaOrdiniRequest();
        request.setOrdineIds(List.of(1L));

        when(ordineRepository.findAllById(List.of(1L)))
                .thenReturn(List.of(ordine));

        assertThrows(
                OrdineConsegnatoException.class,
                () -> ordineService.cancellaOrdini(request)
        );

        verify(ordineRepository, never())
                .deleteAll(anyList());
    }

    @Test
    void nonDeveCancellareNessunOrdineSeUnoEConsegnato() {
        Ordine ordine1 = new Ordine();
        ordine1.setId(1L);
        ordine1.setStatus(StatoOrdineEnum.ORDINATO);

        Ordine ordine2 = new Ordine();
        ordine2.setId(2L);
        ordine2.setStatus(StatoOrdineEnum.CONSEGNATO);

        CancellaOrdiniRequest request =
                new CancellaOrdiniRequest();

        request.setOrdineIds(List.of(1L, 2L));

        when(ordineRepository.findAllById(List.of(1L, 2L)))
                .thenReturn(List.of(ordine1, ordine2));

        assertThrows(
                OrdineConsegnatoException.class,
                () -> ordineService.cancellaOrdini(request)
        );

        verify(ordineRepository, never())
                .deleteAll(anyList());
    }
}