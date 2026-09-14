package it.pierlorenzo.ecommerce.service;

import it.pierlorenzo.ecommerce.dto.PageResponse;
import it.pierlorenzo.ecommerce.dto.prodotto.ProdottoRequest;
import it.pierlorenzo.ecommerce.dto.prodotto.ProdottoResponse;
import it.pierlorenzo.ecommerce.mapper.ProdottoMapper;
import it.pierlorenzo.ecommerce.model.prodotto.Prodotto;
import it.pierlorenzo.ecommerce.repository.ProdottoRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class ProdottoService {

    private final ProdottoRepository prodottoRepository;
    private final ProdottoMapper prodottoMapper;

    public ProdottoService(
            ProdottoRepository prodottoRepository,
            ProdottoMapper prodottoMapper) {

        this.prodottoRepository = prodottoRepository;
        this.prodottoMapper = prodottoMapper;
    }

    public ProdottoResponse salvaProdotto(ProdottoRequest request) {

        Prodotto prodotto = prodottoMapper.toEntity(request);

        Prodotto savedProdotto = prodottoRepository.save(prodotto);

        return prodottoMapper.toResponse(savedProdotto);
    }

    public PageResponse<ProdottoResponse> findAll(Pageable pageable) {

        Page<Prodotto> page =
                prodottoRepository.findAll(pageable);

        List<ProdottoResponse> content = page.getContent()
                .stream()
                .map(prodottoMapper::toResponse)
                .toList();

        return new PageResponse<>(
                content,
                page.getNumber(),
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages()
        );
    }
}
