package it.pierlorenzo.ecommerce.model.ordine;

import it.pierlorenzo.ecommerce.model.prodottiPerOrdine.ProdottoPerOrdine;
import it.pierlorenzo.ecommerce.model.StatoOrdineEnum;
import it.pierlorenzo.ecommerce.model.cliente.Cliente;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
public class Ordine {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Cliente cliente;

    @OneToMany(mappedBy = "ordine", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProdottoPerOrdine> prodottoPerOrdineList = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    private StatoOrdineEnum status;

}
