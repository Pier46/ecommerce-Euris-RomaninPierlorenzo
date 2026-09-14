package it.pierlorenzo.ecommerce.repository;

import it.pierlorenzo.ecommerce.model.ordine.Ordine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;


@RepositoryRestResource(exported = false)
public interface OrdineRepository extends JpaRepository<Ordine, Long> {
}
