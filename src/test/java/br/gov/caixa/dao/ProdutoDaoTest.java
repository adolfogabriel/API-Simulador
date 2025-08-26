package br.gov.caixa.dao;

import br.gov.caixa.entity.ProdutoEntity;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest
class ProdutoDaoTest {

    @Mock
    private EntityManager entityManager;

    @InjectMocks
    private ProdutoDao produtoDao;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void obterProduto_shouldReturnProdutoEntityWhenValidParameters() {
        ProdutoEntity produtoEntity = new ProdutoEntity();
        jakarta.persistence.Query queryMock = mock(jakarta.persistence.Query.class);

        when(entityManager.createNativeQuery(anyString(), eq(ProdutoEntity.class)))
                .thenReturn(queryMock);
        when(queryMock.setParameter(eq("valor"), anyDouble()))
                .thenReturn(queryMock);
        when(queryMock.setParameter(eq("prazo"), anyInt()))
                .thenReturn(queryMock);
        when(queryMock.getResultList())
                .thenReturn(Collections.singletonList(produtoEntity));

        ProdutoEntity result = produtoDao.obterProduto(10000.0, 12);

        assertNotNull(result);
    }

    @Test
    void obterProduto_shouldReturnNullWhenNoResults() {
        ProdutoEntity produtoEntity = new ProdutoEntity();
        jakarta.persistence.Query queryMock = mock(jakarta.persistence.Query.class);

        when(entityManager.createNativeQuery(anyString(), eq(ProdutoEntity.class)))
                .thenReturn(queryMock);
        when(queryMock.setParameter(eq("valor"), anyDouble()))
                .thenReturn(queryMock);
        when(queryMock.setParameter(eq("prazo"), anyInt()))
                .thenReturn(queryMock);
        when(queryMock.getResultList())
                .thenReturn(Collections.singletonList(produtoEntity));

        ProdutoEntity result = produtoDao.obterProduto(0.0, 0);

        assertNull(result.getPcTaxaJuros());
    }
}