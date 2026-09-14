package it.pierlorenzo.ecommerce.controller;


import it.pierlorenzo.ecommerce.dto.PageResponse;
import it.pierlorenzo.ecommerce.dto.prodotto.ProdottoRequest;
import it.pierlorenzo.ecommerce.dto.prodotto.ProdottoResponse;
import it.pierlorenzo.ecommerce.service.ProdottoService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProdottoRestController.class)
class ProdottoRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private ProdottoService prodottoService;

    @Test
    void creaProdottoTest() throws Exception {

        ProdottoRequest request = new ProdottoRequest();
        request.setCodice("P001");
        request.setNome("Laptop");
        request.setStock(10);

        ProdottoResponse response = new ProdottoResponse();
        response.setId(1L);
        response.setCodice("P001");
        response.setNome("Laptop");
        response.setStock(10);

        when(prodottoService.salvaProdotto(any(ProdottoRequest.class)))
                .thenReturn(response);

        mockMvc.perform(
                        post("/api/prodotti")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.codice").value("P001"))
                .andExpect(jsonPath("$.nome").value("Laptop"))
                .andExpect(jsonPath("$.stock").value(10));
    }

    @Test
    void prodottoNonValidoTest() throws Exception {

        ProdottoRequest request = new ProdottoRequest();

        mockMvc.perform(
                        post("/api/prodotti")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isBadRequest());
    }

    @Test
    void getPagingProdottiTest() throws Exception {

        ProdottoResponse prodotto = new ProdottoResponse();
        prodotto.setId(1L);
        prodotto.setCodice("P001");
        prodotto.setNome("Laptop");
        prodotto.setStock(10);

        PageResponse<ProdottoResponse> page =
                new PageResponse<>(
                        List.of(prodotto),
                        0,
                        10,
                        1,
                        1
                );

        when(prodottoService.findAll(any()))
                .thenReturn(page);

        mockMvc.perform(
                        get("/api/prodotti")
                                .param("page", "0")
                                .param("size", "10")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(1))
                .andExpect(jsonPath("$.content[0].codice").value("P001"))
                .andExpect(jsonPath("$.content[0].nome").value("Laptop"))
                .andExpect(jsonPath("$.content[0].stock").value(10));
    }
}