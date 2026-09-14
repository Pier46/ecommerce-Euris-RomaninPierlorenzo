package it.pierlorenzo.ecommerce.factory;

import it.pierlorenzo.ecommerce.model.prodottiPerOrdine.ProdottoPerOrdine;
import it.pierlorenzo.ecommerce.model.StatoOrdineEnum;
import it.pierlorenzo.ecommerce.model.cliente.Cliente;
import it.pierlorenzo.ecommerce.model.ordine.Ordine;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class OrdineFactory {

    public Ordine create(
            Cliente cliente,
            List<ProdottoPerOrdine> prodottiPerOrdine) {

        Ordine ordine = new Ordine();

        ordine.setCliente(cliente);
        ordine.setStatus(StatoOrdineEnum.ORDINATO);
        ordine.setProdottoPerOrdineList(prodottiPerOrdine);

        for (ProdottoPerOrdine prodottoPerOrdine : prodottiPerOrdine) {
            prodottoPerOrdine.setOrdine(ordine);
        }

        return ordine;
    }
}