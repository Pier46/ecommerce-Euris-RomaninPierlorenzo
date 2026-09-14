package it.pierlorenzo.ecommerce.service;

import it.pierlorenzo.ecommerce.dto.cliente.ClienteRequest;
import it.pierlorenzo.ecommerce.dto.cliente.ClienteResponse;
import it.pierlorenzo.ecommerce.mapper.ClienteMapper;
import it.pierlorenzo.ecommerce.model.cliente.Cliente;
import it.pierlorenzo.ecommerce.repository.ClienteRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ClienteServiceTest {

    @Mock
    private ClienteRepository clienteRepository;

    @Mock
    private ClienteMapper clienteMapper;

    @InjectMocks
    private ClienteService clienteService;

    @Test
    void salvaClienteTest() {

        ClienteRequest request = new ClienteRequest();
        request.setNome("Mario");
        request.setCognome("Rossi");
        request.setDataNascita(LocalDate.of(1980, 1, 1));
        request.setCodFiscale("RSSMRA80A01H501Z");
        request.setEmail("mario.rossi@email.it");

        Cliente cliente = new Cliente();
        ClienteResponse response = new ClienteResponse();

        when(clienteMapper.toEntity(request)).thenReturn(cliente);
        when(clienteRepository.save(cliente)).thenReturn(cliente);
        when(clienteMapper.toResponse(cliente)).thenReturn(response);

        ClienteResponse risultato = clienteService.salvaCliente(request);

        assertNotNull(risultato);
        assertSame(response, risultato);

        verify(clienteMapper).toEntity(request);
        verify(clienteRepository).save(cliente);
        verify(clienteMapper).toResponse(cliente);
    }

    @Test
    void getPagingClienteTest() {

        PageRequest pageable = PageRequest.of(0, 10);

        Cliente cliente = new Cliente();
        ClienteResponse response = new ClienteResponse();

        Page<Cliente> page = new PageImpl<>(List.of(cliente));

        when(clienteRepository.findAll(pageable)).thenReturn(page);
        when(clienteMapper.toResponse(cliente)).thenReturn(response);

        var risultato = clienteService.findAll(pageable);

        assertNotNull(risultato);
        assertEquals(1, risultato.getContent().size());
        assertSame(response, risultato.getContent().get(0));

        verify(clienteRepository).findAll(pageable);
        verify(clienteMapper).toResponse(cliente);
    }
}