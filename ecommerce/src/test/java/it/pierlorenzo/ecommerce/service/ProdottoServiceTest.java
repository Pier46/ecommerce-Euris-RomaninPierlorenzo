package it.pierlorenzo.ecommerce.service;

import it.pierlorenzo.ecommerce.dto.prodotto.ProdottoRequest;
import it.pierlorenzo.ecommerce.dto.prodotto.ProdottoResponse;
import it.pierlorenzo.ecommerce.mapper.ProdottoMapper;
import it.pierlorenzo.ecommerce.model.prodotto.Prodotto;
import it.pierlorenzo.ecommerce.repository.ProdottoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProdottoServiceTest {

    @Mock
    private ProdottoRepository prodottoRepository;

    @Mock
    private ProdottoMapper prodottoMapper;

    @InjectMocks
    private ProdottoService prodottoService;

    @Test
    void salvaProdottoTest() {

        ProdottoRequest request = new ProdottoRequest();
        request.setCodice("P001");
        request.setNome("Prodotto 1");
        request.setStock(10);

        Prodotto prodotto = new Prodotto();
        ProdottoResponse response = new ProdottoResponse();

        when(prodottoMapper.toEntity(request)).thenReturn(prodotto);
        when(prodottoRepository.save(prodotto)).thenReturn(prodotto);
        when(prodottoMapper.toResponse(prodotto)).thenReturn(response);

        ProdottoResponse risultato = prodottoService.salvaProdotto(request);

        assertNotNull(risultato);
        assertSame(response, risultato);

        verify(prodottoMapper).toEntity(request);
        verify(prodottoRepository).save(prodotto);
        verify(prodottoMapper).toResponse(prodotto);
    }

    @Test
    void getPagingProdottoTest() {

        PageRequest pageable = PageRequest.of(0, 10);

        Prodotto prodotto = new Prodotto();
        ProdottoResponse response = new ProdottoResponse();

        Page<Prodotto> page = new PageImpl<>(List.of(prodotto));

        when(prodottoRepository.findAll(pageable)).thenReturn(page);
        when(prodottoMapper.toResponse(prodotto)).thenReturn(response);

        var risultato = prodottoService.findAll(pageable);

        assertNotNull(risultato);
        assertEquals(1, risultato.getContent().size());
        assertSame(response, risultato.getContent().get(0));

        verify(prodottoRepository).findAll(pageable);
        verify(prodottoMapper).toResponse(prodotto);
    }
}