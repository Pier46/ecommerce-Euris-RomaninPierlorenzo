package it.pierlorenzo.ecommerce.controller;


import it.pierlorenzo.ecommerce.dto.PageResponse;
import it.pierlorenzo.ecommerce.dto.cliente.ClienteRequest;
import it.pierlorenzo.ecommerce.dto.cliente.ClienteResponse;
import it.pierlorenzo.ecommerce.service.ClienteService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ClienteRestController.class)
class ClienteRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private ClienteService clienteService;

    @Test
    void creaClienteTest() throws Exception {

        ClienteRequest request = new ClienteRequest();
        request.setNome("Mario");
        request.setCognome("Rossi");
        request.setDataNascita(LocalDate.of(1980, 1, 1));
        request.setCodFiscale("RSSMRA80A01H501X");
        request.setEmail("mario.rossi@test.it");

        ClienteResponse response = new ClienteResponse();
        response.setId(1L);
        response.setNome("Mario");
        response.setCognome("Rossi");
        response.setDataNascita(LocalDate.of(1980, 1, 1));
        response.setCodFiscale("RSSMRA80A01H501X");
        response.setEmail("mario.rossi@test.it");

        when(clienteService.salvaCliente(any(ClienteRequest.class)))
                .thenReturn(response);

        mockMvc.perform(
                        post("/api/clienti")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nome").value("Mario"))
                .andExpect(jsonPath("$.cognome").value("Rossi"))
                .andExpect(jsonPath("$.email")
                        .value("mario.rossi@test.it"));
    }

    @Test
    void clienteNonValidoTest() throws Exception {

        ClienteRequest request = new ClienteRequest();

        mockMvc.perform(
                        post("/api/clienti")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isBadRequest());
    }

    @Test
    void getPagingClienteTest() throws Exception {

        ClienteResponse cliente = new ClienteResponse();
        cliente.setId(1L);
        cliente.setNome("Mario");
        cliente.setCognome("Rossi");

        PageResponse<ClienteResponse> page =
                new PageResponse<>(
                        List.of(cliente),
                        0,
                        10,
                        1,
                        1
                );

        when(clienteService.findAll(any()))
                .thenReturn(page);

        mockMvc.perform(
                        get("/api/clienti")
                                .param("page", "0")
                                .param("size", "10")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(1))
                .andExpect(jsonPath("$.content[0].nome").value("Mario"))
                .andExpect(jsonPath("$.content[0].cognome").value("Rossi"));
    }
}