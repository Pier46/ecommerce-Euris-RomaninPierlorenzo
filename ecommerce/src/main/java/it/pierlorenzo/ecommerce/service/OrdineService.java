package it.pierlorenzo.ecommerce.service;

import it.pierlorenzo.ecommerce.dto.ordine.AggiornaStatoOrdineRequest;
import it.pierlorenzo.ecommerce.dto.ordine.CancellaOrdiniRequest;
import it.pierlorenzo.ecommerce.dto.ordine.OrdineRequest;
import it.pierlorenzo.ecommerce.dto.ordine.OrdineResponse;
import it.pierlorenzo.ecommerce.dto.PageResponse;
import it.pierlorenzo.ecommerce.dto.prodottoPerOrdine.ProdottoPerOrdineRequest;
import it.pierlorenzo.ecommerce.exception.*;
import it.pierlorenzo.ecommerce.factory.OrdineFactory;
import it.pierlorenzo.ecommerce.mapper.OrdineMapper;
import it.pierlorenzo.ecommerce.model.StatoOrdineEnum;
import it.pierlorenzo.ecommerce.model.cliente.Cliente;
import it.pierlorenzo.ecommerce.model.ordine.Ordine;
import it.pierlorenzo.ecommerce.model.prodottiPerOrdine.ProdottoPerOrdine;
import it.pierlorenzo.ecommerce.model.prodotto.Prodotto;
import it.pierlorenzo.ecommerce.repository.ClienteRepository;
import it.pierlorenzo.ecommerce.repository.OrdineRepository;
import it.pierlorenzo.ecommerce.repository.ProdottoRepository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.resilience.annotation.Retryable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class OrdineService {

    private final OrdineRepository ordineRepository;
    private final ClienteRepository clienteRepository;
    private final ProdottoRepository prodottoRepository;
    private final OrdineFactory ordineFactory;
    private final OrdineMapper ordineMapper;

    public OrdineService(
            OrdineRepository ordineRepository,
            ClienteRepository clienteRepository,
            ProdottoRepository prodottoRepository,
            OrdineFactory ordineFactory,
            OrdineMapper ordineMapper) {

        this.ordineRepository = ordineRepository;
        this.clienteRepository = clienteRepository;
        this.prodottoRepository = prodottoRepository;
        this.ordineFactory = ordineFactory;
        this.ordineMapper = ordineMapper;
    }

    @Retryable(
            includes = ObjectOptimisticLockingFailureException.class,
            maxRetries = 2,
            delay = 100
    )
    @Transactional
    public OrdineResponse creaOrdine(OrdineRequest request) {

        Cliente cliente = clienteRepository.findById(request.getClienteId())
                .orElseThrow(() ->
                        new ClienteNonTrovatoException("Cliente non trovato"));

        List<ProdottoPerOrdine> prodottiPerOrdine = new ArrayList<>();

        for (ProdottoPerOrdineRequest itemRequest : request.getProdotti()) {

            Prodotto prodotto = prodottoRepository
                    .findById(itemRequest.getProdottoId())
                    .orElseThrow(() ->
                            new ProdottoNonTrovatoException("Prodotto non trovato"));

            if (prodotto.getStock() < itemRequest.getQuantita()) {
                throw new StockInsufficienteException(
                        "Stock insufficiente per il prodotto: "
                                + prodotto.getCodice());
            }

            prodotto.setStock(
                    prodotto.getStock() - itemRequest.getQuantita()
            );

            ProdottoPerOrdine prodottoPerOrdine =
                    new ProdottoPerOrdine();

            prodottoPerOrdine.setProdotto(prodotto);
            prodottoPerOrdine.setQuantita(itemRequest.getQuantita());

            prodottiPerOrdine.add(prodottoPerOrdine);
        }

        Ordine ordine = ordineFactory.create(
                cliente,
                prodottiPerOrdine
        );

        Ordine savedOrdine = ordineRepository.save(ordine);

        return ordineMapper.toResponse(savedOrdine);
    }

    public PageResponse<OrdineResponse> findAll(Pageable pageable) {

        Page<Ordine> page = ordineRepository.findAll(pageable);

        List<OrdineResponse> content = page.getContent()
                .stream()
                .map(ordineMapper::toResponse)
                .toList();

        return new PageResponse<>(
                content,
                page.getNumber(),
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages()
        );
    }

    public OrdineResponse aggiornaStato(Long ordineId, AggiornaStatoOrdineRequest request) {

        Ordine ordine = ordineRepository.findById(ordineId)
                .orElseThrow(() -> new OrdineNonTrovatoException("Ordine non trovato"));

        if (ordine.getStatus() == StatoOrdineEnum.CONSEGNATO
                && request.getStatus() == StatoOrdineEnum.ORDINATO) {

            throw new StatoOrdineNonValidoException(
                    "Non è possibile riportare un ordine consegnato allo stato ordinato"
            );
        }

        ordine.setStatus(request.getStatus());

        Ordine savedOrdine = ordineRepository.save(ordine);

        return ordineMapper.toResponse(savedOrdine);
    }

    @Transactional
    public void cancellaOrdini(CancellaOrdiniRequest request) {

        List<Ordine> ordini = ordineRepository.findAllById(request.getOrdineIds());

        if (ordini.size() != request.getOrdineIds().size()) {
            throw new OrdineNonTrovatoException(
                    "Uno o più ordini non sono stati trovati"
            );
        }

        for (Ordine ordine : ordini) {

            if (ordine.getStatus() == StatoOrdineEnum.CONSEGNATO) {
                throw new OrdineConsegnatoException(
                        "Non è possibile proseguire con la cancellazione: uno o più ordini selezionati sono già in stato CONSEGNATO"
                );
            }

            for (ProdottoPerOrdine prodottoPerOrdine :
                    ordine.getProdottoPerOrdineList()) {

                Prodotto prodotto = prodottoPerOrdine.getProdotto();

                prodotto.setStock(
                        prodotto.getStock() + prodottoPerOrdine.getQuantita()
                );
            }
        }

        ordineRepository.deleteAll(ordini);
    }
}