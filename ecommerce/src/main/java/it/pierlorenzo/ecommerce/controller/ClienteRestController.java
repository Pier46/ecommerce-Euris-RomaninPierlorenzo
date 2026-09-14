package it.pierlorenzo.ecommerce.controller;

import it.pierlorenzo.ecommerce.dto.PageResponse;
import it.pierlorenzo.ecommerce.dto.cliente.ClienteRequest;
import it.pierlorenzo.ecommerce.dto.cliente.ClienteResponse;
import it.pierlorenzo.ecommerce.service.ClienteService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/clienti")
public class ClienteRestController {

    private final ClienteService clienteService;

    public ClienteRestController(ClienteService clienteService) {
        this.clienteService = clienteService;
    }

    @GetMapping
    public PageResponse<ClienteResponse> getClienti(
            Pageable pageable) {

        return clienteService.findAll(pageable);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ClienteResponse createCliente(
            @Valid @RequestBody ClienteRequest request) {

        return clienteService.salvaCliente(request);
    }
}
