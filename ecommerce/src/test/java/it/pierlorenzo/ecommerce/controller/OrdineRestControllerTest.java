package it.pierlorenzo.ecommerce.controller;

import it.pierlorenzo.ecommerce.dto.PageResponse;
import it.pierlorenzo.ecommerce.dto.ordine.AggiornaStatoOrdineRequest;
import it.pierlorenzo.ecommerce.dto.ordine.CancellaOrdiniRequest;
import it.pierlorenzo.ecommerce.dto.ordine.OrdineRequest;
import it.pierlorenzo.ecommerce.dto.ordine.OrdineResponse;
import it.pierlorenzo.ecommerce.exception.ClienteNonTrovatoException;
import it.pierlorenzo.ecommerce.exception.StatoOrdineNonValidoException;
import it.pierlorenzo.ecommerce.exception.StockInsufficienteException;
import it.pierlorenzo.ecommerce.service.OrdineService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;

import org.springframework.http.MediaType;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(OrdineRestController.class)
class OrdineRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private OrdineService ordineService;

    @Test
    void creaOrdineTest() throws Exception {

        OrdineResponse response = new OrdineResponse();
        response.setId(1L);
        response.setCliente("Mario Rossi");
        response.setStatus("ORDINATO");

        when(ordineService.creaOrdine(any(OrdineRequest.class)))
                .thenReturn(response);

        String json = """
        {
            "clienteId": 1,
            "prodotti": [
                {
                    "prodottoId": 1,
                    "quantita": 3
                }
            ]
        }
        """;

        mockMvc.perform(
                        post("/api/ordini")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(json)
                )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.cliente").value("Mario Rossi"))
                .andExpect(jsonPath("$.status").value("ORDINATO"));
    }

    @Test
    void creaOrdineQuantitaNonValidaTest() throws Exception {

        String json = """
        {
            "clienteId": 1,
            "prodotti": [
                {
                    "prodottoId": 1,
                    "quantita": 0
                }
            ]
        }
        """;

        mockMvc.perform(
                        post("/api/ordini")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(json)
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.error").value("Bad Request"))
                .andExpect(jsonPath("$.message")
                        .value("prodotti[0].quantita: must be greater than 0"));

        verify(ordineService, never())
                .creaOrdine(any(OrdineRequest.class));
    }

    @Test
    void creaOrdineClienteNonTrovatoTest() throws Exception {

        when(ordineService.creaOrdine(any(OrdineRequest.class)))
                .thenThrow(
                        new ClienteNonTrovatoException(
                                "Cliente non trovato: 999"
                        )
                );

        String json = """
        {
            "clienteId": 999,
            "prodotti": [
                {
                    "prodottoId": 1,
                    "quantita": 3
                }
            ]
        }
        """;

        mockMvc.perform(
                        post("/api/ordini")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(json)
                )
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.error").value("Not Found"))
                .andExpect(jsonPath("$.message")
                        .value("Cliente non trovato: 999"));
    }

    @Test
    void creaOrdineStockInsufficienteTest() throws Exception {

        when(ordineService.creaOrdine(any(OrdineRequest.class)))
                .thenThrow(
                        new StockInsufficienteException(
                                "Stock insufficiente per il prodotto: P001"
                        )
                );

        String json = """
        {
            "clienteId": 1,
            "prodotti": [
                {
                    "prodottoId": 1,
                    "quantita": 11
                }
            ]
        }
        """;

        mockMvc.perform(
                        post("/api/ordini")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(json)
                )
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.status").value(409))
                .andExpect(jsonPath("$.error").value("Conflict"))
                .andExpect(jsonPath("$.message")
                        .value("Stock insufficiente per il prodotto: P001"));
    }

    @Test
    void aggiornaStatoOrdineTest() throws Exception {

        OrdineResponse response = new OrdineResponse();
        response.setId(1L);
        response.setCliente("Mario Rossi");
        response.setStatus("CONSEGNATO");

        when(ordineService.aggiornaStato(
                any(Long.class),
                any(AggiornaStatoOrdineRequest.class)
        )).thenReturn(response);

        String json = """
        {
            "status": "CONSEGNATO"
        }
        """;

        mockMvc.perform(
                        put("/api/ordini/1/stato")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(json)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.cliente").value("Mario Rossi"))
                .andExpect(jsonPath("$.status").value("CONSEGNATO"));
    }

    @Test
    void cambioStatoBloccatoTest() throws Exception {

        when(ordineService.aggiornaStato(
                any(Long.class),
                any(AggiornaStatoOrdineRequest.class)
        )).thenThrow(
                new StatoOrdineNonValidoException(
                        "Non è possibile riportare un ordine consegnato allo stato ordinato"
                )
        );

        String json = """
        {
            "status": "ORDINATO"
        }
        """;

        mockMvc.perform(
                        put("/api/ordini/1/stato")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(json)
                )
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.status").value(409))
                .andExpect(jsonPath("$.error").value("Conflict"))
                .andExpect(jsonPath("$.message")
                        .value("Non è possibile riportare un ordine consegnato allo stato ordinato"));
    }

    @Test
    void cancellaOrdiniTest() throws Exception {

        String json = """
        {
            "ordineIds": [1, 2, 3]
        }
        """;

        mockMvc.perform(
                        delete("/api/ordini")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(json)
                )
                .andExpect(status().isNoContent());

        verify(ordineService).cancellaOrdini(any(CancellaOrdiniRequest.class));
    }

    @Test
    void getPagingOrdiniTest() throws Exception {

        OrdineResponse ordine1 = new OrdineResponse();
        ordine1.setId(1L);
        ordine1.setCliente("Mario Rossi");
        ordine1.setStatus("ORDINATO");

        OrdineResponse ordine2 = new OrdineResponse();
        ordine2.setId(2L);
        ordine2.setCliente("Luigi Bianchi");
        ordine2.setStatus("CONSEGNATO");

        PageResponse<OrdineResponse> response =
                new PageResponse<>(
                        List.of(ordine1, ordine2),
                        0,
                        2,
                        5,
                        3
                );

        when(ordineService.findAll(any(Pageable.class)))
                .thenReturn(response);

        mockMvc.perform(
                        get("/api/ordini?page=0&size=2")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(2))
                .andExpect(jsonPath("$.content[0].id").value(1))
                .andExpect(jsonPath("$.content[1].id").value(2))
                .andExpect(jsonPath("$.page").value(0))
                .andExpect(jsonPath("$.size").value(2))
                .andExpect(jsonPath("$.totalElements").value(5))
                .andExpect(jsonPath("$.totalPages").value(3));
    }
}