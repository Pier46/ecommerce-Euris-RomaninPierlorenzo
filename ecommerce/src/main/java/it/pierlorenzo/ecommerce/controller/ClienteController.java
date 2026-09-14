package it.pierlorenzo.ecommerce.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ClienteController {

    @GetMapping("/clienti")
    public String clienti(Model model) {
        return "clienti";
    }
}
