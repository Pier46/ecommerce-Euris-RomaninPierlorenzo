package it.pierlorenzo.ecommerce.controller;

import it.pierlorenzo.ecommerce.dto.PageResponse;
import it.pierlorenzo.ecommerce.dto.prodotto.ProdottoRequest;
import it.pierlorenzo.ecommerce.dto.prodotto.ProdottoResponse;
import it.pierlorenzo.ecommerce.service.ProdottoService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/prodotti")
public class ProdottoRestController {

    private final ProdottoService prodottoService;

    public ProdottoRestController(ProdottoService prodottoService) {
        this.prodottoService = prodottoService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ProdottoResponse createProdotto(
            @Valid @RequestBody ProdottoRequest request) {

        return prodottoService.salvaProdotto(request);
    }

    @GetMapping
    public PageResponse<ProdottoResponse> getProdotti(
            Pageable pageable) {

        return prodottoService.findAll(pageable);
    }
}
