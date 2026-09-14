package it.pierlorenzo.ecommerce.service;

import it.pierlorenzo.ecommerce.dto.PageResponse;
import it.pierlorenzo.ecommerce.dto.cliente.ClienteRequest;
import it.pierlorenzo.ecommerce.dto.cliente.ClienteResponse;
import it.pierlorenzo.ecommerce.mapper.ClienteMapper;
import it.pierlorenzo.ecommerce.model.cliente.Cliente;
import it.pierlorenzo.ecommerce.repository.ClienteRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class ClienteService {

    private final ClienteRepository clienteRepository;
    private final ClienteMapper clienteMapper;

    public ClienteService(ClienteRepository clienteRepository,
                          ClienteMapper clienteMapper) {

        this.clienteRepository = clienteRepository;
        this.clienteMapper = clienteMapper;
    }

    public PageResponse<ClienteResponse> findAll(Pageable pageable) {

        Page<Cliente> page = clienteRepository.findAll(pageable);

        List<ClienteResponse> content = page.getContent()
                .stream()
                .map(clienteMapper::toResponse)
                .toList();

        return new PageResponse<>(
                content,
                page.getNumber(),
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages()
        );
    }

    public ClienteResponse salvaCliente(ClienteRequest request) {

        Cliente cliente = clienteMapper.toEntity(request);
        Cliente savedCliente = clienteRepository.save(cliente);

        return clienteMapper.toResponse(savedCliente);
    }
}