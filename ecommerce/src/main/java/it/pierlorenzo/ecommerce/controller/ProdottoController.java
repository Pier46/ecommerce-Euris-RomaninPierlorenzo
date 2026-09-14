package it.pierlorenzo.ecommerce.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ProdottoController {

    @GetMapping("/prodotti")
    public String prodotti(Model model) {
        return "prodotti";
    }
}
