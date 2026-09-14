package it.pierlorenzo.ecommerce.model.prodottiPerOrdine;

import it.pierlorenzo.ecommerce.model.ordine.Ordine;
import it.pierlorenzo.ecommerce.model.prodotto.Prodotto;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
public class ProdottoPerOrdine {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int quantita;

    @ManyToOne
    private Prodotto prodotto;

    @ManyToOne
    private Ordine ordine;
}
