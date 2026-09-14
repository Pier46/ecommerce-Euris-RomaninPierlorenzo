package it.pierlorenzo.ecommerce.controller;

import it.pierlorenzo.ecommerce.dto.PageResponse;
import it.pierlorenzo.ecommerce.dto.ordine.AggiornaStatoOrdineRequest;
import it.pierlorenzo.ecommerce.dto.ordine.CancellaOrdiniRequest;
import it.pierlorenzo.ecommerce.dto.ordine.OrdineRequest;
import it.pierlorenzo.ecommerce.dto.ordine.OrdineResponse;
import it.pierlorenzo.ecommerce.service.OrdineService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/ordini")
public class OrdineRestController {

    private final OrdineService ordineService;

    public OrdineRestController(OrdineService ordineService) {
        this.ordineService = ordineService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public OrdineResponse  creaOrdine(@Valid @RequestBody OrdineRequest request) {
        return ordineService.creaOrdine(request);
    }

    @GetMapping
    public PageResponse<OrdineResponse> getOrdini(Pageable pageable) {
        return ordineService.findAll(pageable);
    }

    @PutMapping("/{id}/stato")
    public OrdineResponse aggiornaStato(
            @PathVariable Long id,
            @Valid @RequestBody AggiornaStatoOrdineRequest request) {

        return ordineService.aggiornaStato(id, request);
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void cancellaOrdini(@Valid @RequestBody CancellaOrdiniRequest request) {

        ordineService.cancellaOrdini(request);
    }
}