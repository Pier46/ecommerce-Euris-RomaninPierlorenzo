package it.pierlorenzo.ecommerce.dto.ordine;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class CancellaOrdiniRequest {

    @NotEmpty
    private List<@NotNull Long> ordineIds;
}
