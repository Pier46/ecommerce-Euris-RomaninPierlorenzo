package it.pierlorenzo.ecommerce.mapper;

import it.pierlorenzo.ecommerce.dto.cliente.ClienteRequest;
import it.pierlorenzo.ecommerce.dto.cliente.ClienteResponse;
import it.pierlorenzo.ecommerce.model.cliente.Cliente;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class ClienteMapperTest {

    private final ClienteMapper clienteMapper = new ClienteMapper();

    @Test
    void requestToEntityTest() {

        ClienteRequest request = new ClienteRequest();
        request.setNome("Mario");
        request.setCognome("Rossi");
        request.setDataNascita(LocalDate.of(1980, 1, 1));
        request.setCodFiscale("RSSMRA80A01H501Z");
        request.setEmail("mario.rossi@email.it");

        Cliente risultato = clienteMapper.toEntity(request);

        assertNotNull(risultato);
        assertEquals("Mario", risultato.getNome());
        assertEquals("Rossi", risultato.getCognome());
        assertEquals(
                LocalDate.of(1980, 1, 1),
                risultato.getDataNascita()
        );
        assertEquals(
                "RSSMRA80A01H501Z",
                risultato.getCodFiscale()
        );
        assertEquals(
                "mario.rossi@email.it",
                risultato.getEmail()
        );
    }

    @Test
    void entityToResponseTest() {

        Cliente cliente = new Cliente();
        cliente.setId(1L);
        cliente.setNome("Mario");
        cliente.setCognome("Rossi");
        cliente.setDataNascita(LocalDate.of(1980, 1, 1));
        cliente.setCodFiscale("RSSMRA80A01H501Z");
        cliente.setEmail("mario.rossi@email.it");

        ClienteResponse risultato = clienteMapper.toResponse(cliente);

        assertNotNull(risultato);
        assertEquals(1L, risultato.getId());
        assertEquals("Mario", risultato.getNome());
        assertEquals("Rossi", risultato.getCognome());
        assertEquals(
                LocalDate.of(1980, 1, 1),
                risultato.getDataNascita()
        );
        assertEquals(
                "RSSMRA80A01H501Z",
                risultato.getCodFiscale()
        );
        assertEquals(
                "mario.rossi@email.it",
                risultato.getEmail()
        );
    }
}