package it.pierlorenzo.ecommerce.repository;

import it.pierlorenzo.ecommerce.model.prodotto.Prodotto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(exported = false)
public interface ProdottoRepository extends JpaRepository<Prodotto, Long> {
}
