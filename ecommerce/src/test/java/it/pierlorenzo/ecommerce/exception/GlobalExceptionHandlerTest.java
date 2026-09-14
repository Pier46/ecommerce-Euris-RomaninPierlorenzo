package it.pierlorenzo.ecommerce.exception;

import it.pierlorenzo.ecommerce.dto.ErroreResponse;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;

class GlobalExceptionHandlerTest {

    @Test
    void clienteNonTrovatoTest() {

        GlobalExceptionHandler handler = new GlobalExceptionHandler();

        ResponseEntity<ErroreResponse> response =
                handler.handleClienteNonTrovato(
                        new ClienteNonTrovatoException("Cliente non trovato")
                );

        ErroreResponse body = response.getBody();

        assertNotNull(body);
        assertEquals(HttpStatus.NOT_FOUND.value(), body.getStatus());
        assertEquals("Not Found", body.getError());
        assertEquals("Cliente non trovato", body.getMessage());
        assertNotNull(body.getTimestamp());
    }

    @Test
    void prodottoNonTrovatoTest() {

        GlobalExceptionHandler handler = new GlobalExceptionHandler();

        ResponseEntity<ErroreResponse> response =
                handler.handleProdottoNonTrovato(
                        new ProdottoNonTrovatoException("Prodotto non trovato")
                );

        ErroreResponse body = response.getBody();

        assertNotNull(body);
        assertEquals(HttpStatus.NOT_FOUND.value(), body.getStatus());
        assertEquals("Not Found", body.getError());
        assertEquals("Prodotto non trovato", body.getMessage());
        assertNotNull(body.getTimestamp());
    }

    @Test
    void ordineNonTrovatoTest() {

        GlobalExceptionHandler handler = new GlobalExceptionHandler();

        ResponseEntity<ErroreResponse> response =
                handler.handleOrdineNonTrovato(
                        new OrdineNonTrovatoException("Ordine non trovato")
                );

        ErroreResponse body = response.getBody();

        assertNotNull(body);
        assertEquals(HttpStatus.NOT_FOUND.value(), body.getStatus());
        assertEquals("Not Found", body.getError());
        assertEquals("Ordine non trovato", body.getMessage());
        assertNotNull(body.getTimestamp());
    }

    @Test
    void stockInsufficienteTest() {

        GlobalExceptionHandler handler = new GlobalExceptionHandler();

        ResponseEntity<ErroreResponse> response =
                handler.handleStockInsufficiente(
                        new StockInsufficienteException("Stock insufficiente")
                );

        ErroreResponse body = response.getBody();

        assertNotNull(body);
        assertEquals(HttpStatus.CONFLICT.value(), body.getStatus());
        assertEquals("Conflict", body.getError());
        assertEquals("Stock insufficiente", body.getMessage());
        assertNotNull(body.getTimestamp());
    }

    @Test
    void ordineConsegnatoTest() {

        GlobalExceptionHandler handler = new GlobalExceptionHandler();

        ResponseEntity<ErroreResponse> response =
                handler.handleOrdineConsegnato(
                        new OrdineConsegnatoException(
                                "L'ordine è già stato consegnato"
                        )
                );

        ErroreResponse body = response.getBody();

        assertNotNull(body);
        assertEquals(HttpStatus.CONFLICT.value(), body.getStatus());
        assertEquals("Conflict", body.getError());
        assertEquals(
                "L'ordine è già stato consegnato",
                body.getMessage()
        );
        assertNotNull(body.getTimestamp());
    }

    @Test
    void statoOrdineNonValidoTest() {

        GlobalExceptionHandler handler = new GlobalExceptionHandler();

        ResponseEntity<ErroreResponse> response =
                handler.handleStatoOrdineNonValido(
                        new StatoOrdineNonValidoException(
                                "Stato ordine non valido"
                        )
                );

        ErroreResponse body = response.getBody();

        assertNotNull(body);
        assertEquals(HttpStatus.CONFLICT.value(), body.getStatus());
        assertEquals("Conflict", body.getError());
        assertEquals("Stato ordine non valido", body.getMessage());
        assertNotNull(body.getTimestamp());
    }
}