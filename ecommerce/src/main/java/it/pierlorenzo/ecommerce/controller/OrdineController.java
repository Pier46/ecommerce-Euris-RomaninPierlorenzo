package it.pierlorenzo.ecommerce.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class OrdineController {

    @GetMapping("/ordini")
    public String ordini() {
        return "ordini";
    }
}