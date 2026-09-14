package it.pierlorenzo.ecommerce.mapper;

import it.pierlorenzo.ecommerce.dto.cliente.ClienteRequest;
import it.pierlorenzo.ecommerce.dto.cliente.ClienteResponse;
import it.pierlorenzo.ecommerce.model.cliente.Cliente;
import org.springframework.stereotype.Component;

@Component
public class ClienteMapper {

    public Cliente toEntity(ClienteRequest request) {

        Cliente cliente = new Cliente();

        cliente.setNome(request.getNome());
        cliente.setCognome(request.getCognome());
        cliente.setDataNascita(request.getDataNascita());
        cliente.setCodFiscale(request.getCodFiscale());
        cliente.setEmail(request.getEmail());

        return cliente;
    }

    public ClienteResponse toResponse(Cliente cliente) {

        ClienteResponse response = new ClienteResponse();

        response.setId(cliente.getId());
        response.setNome(cliente.getNome());
        response.setCognome(cliente.getCognome());
        response.setDataNascita(cliente.getDataNascita());
        response.setCodFiscale(cliente.getCodFiscale());
        response.setEmail(cliente.getEmail());

        return response;
    }
}