package it.pierlorenzo.ecommerce.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest({
        HomeController.class,
        ClienteController.class,
        ProdottoController.class,
        OrdineController.class
})
class MvcControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void homeTest() throws Exception {

        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name("index"));
    }

    @Test
    void paginaClientiTest() throws Exception {

        mockMvc.perform(get("/clienti"))
                .andExpect(status().isOk())
                .andExpect(view().name("clienti"));
    }

    @Test
    void paginaProdottiTest() throws Exception {

        mockMvc.perform(get("/prodotti"))
                .andExpect(status().isOk())
                .andExpect(view().name("prodotti"));
    }

    @Test
    void paginaOrdiniTest() throws Exception {

        mockMvc.perform(get("/ordini"))
                .andExpect(status().isOk())
                .andExpect(view().name("ordini"));
    }
}