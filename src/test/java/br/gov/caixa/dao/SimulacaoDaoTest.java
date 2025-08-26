package br.gov.caixa.dao;

import br.gov.caixa.h2.model.ParcelaEntry;
import br.gov.caixa.model.SimulacaoRegistroDto;
import br.gov.caixa.model.SimulacaoResumoDto;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class SimulacaoDaoTest {

    @Mock
    private EntityManager entityManager;

    @InjectMocks
    private SimulacaoDao simulacaoDao;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void listarSimulacoesComTotalParcelas_shouldReturnDtosForValidInputs() {
        Object[] row = {1L, 12, BigDecimal.valueOf(12000.00), BigDecimal.valueOf(10000.00)};
        jakarta.persistence.Query queryMock = mock(jakarta.persistence.Query.class);

        when(entityManager.createNativeQuery(anyString())).thenReturn(queryMock);
        when(queryMock.setParameter(eq(1), anyInt())).thenReturn(queryMock);
        when(queryMock.setParameter(eq(2), anyInt())).thenReturn(queryMock);
        when(queryMock.getResultList()).thenReturn(Collections.singletonList(row));

        List<SimulacaoRegistroDto> result = simulacaoDao.listarSimulacoesComTotalParcelas(1, 10);

        assertEquals(1, result.size());
        assertEquals(1L, result.get(0).getIdSimulacao());
        assertEquals(12, result.get(0).getPrazo());
        assertEquals(12000.00, result.get(0).getValorTotalParcelas());
        assertEquals(10000.00, result.get(0).getValorDesejado());
    }

    @Test
    void listarSimulacoesComTotalParcelas_shouldReturnEmptyListWhenNoResults() {
        Object[] row = {1L, 12, BigDecimal.valueOf(12000.00), BigDecimal.valueOf(10000.00)};
        jakarta.persistence.Query queryMock = mock(jakarta.persistence.Query.class);

        when(entityManager.createNativeQuery(anyString())).thenReturn(queryMock);
        when(queryMock.setParameter(eq(1), anyInt())).thenReturn(queryMock);
        when(queryMock.setParameter(eq(2), anyInt())).thenReturn(queryMock);

        List<SimulacaoRegistroDto> result = simulacaoDao.listarSimulacoesComTotalParcelas(1, 10);

        assertTrue(result.isEmpty());
    }

    @Test
    void listarSimulacoesPorDataProduto_shouldReturnEmptyListWhenNoResults() {
        jakarta.persistence.Query queryMock = mock(jakarta.persistence.Query.class);

        when(entityManager.createNativeQuery(anyString())).thenReturn(queryMock);
        when(queryMock.setParameter(eq(1), anyString())).thenReturn(queryMock);
        when(queryMock.getResultList()).thenReturn(Collections.emptyList());

        List<SimulacaoResumoDto> result = simulacaoDao.listarSimulacoesPorDataProduto("2023-10-01");

        assertTrue(result.isEmpty());
    }

    @Test
    void contarTotalSimulacoes_shouldReturnCorrectCount() {
        when(entityManager.createNativeQuery(anyString()))
                .thenReturn(mock(jakarta.persistence.Query.class));
        when(entityManager.createNativeQuery(anyString()).getSingleResult())
                .thenReturn(100L);

        int result = simulacaoDao.contarTotalSimulacoes();

        assertEquals(100, result);
    }

    @Test
    void getParcelas_shouldReturnParcelasForValidId() {
        ParcelaEntry parcela = new ParcelaEntry();
        jakarta.persistence.Query queryMock = mock(jakarta.persistence.Query.class);

        when(entityManager.createNativeQuery(anyString(), eq(ParcelaEntry.class))).thenReturn(queryMock);
        when(queryMock.setParameter(eq("idSimulacao"), anyLong())).thenReturn(queryMock);
        when(queryMock.getResultList()).thenReturn(Collections.singletonList(parcela));

        List<ParcelaEntry> result = simulacaoDao.getParcelas(1L);

        assertEquals(1, result.size());
        assertEquals(parcela, result.get(0));
    }

    @Test
    void getParcelas_shouldReturnEmptyListWhenNoResults() {
        jakarta.persistence.Query queryMock = mock(jakarta.persistence.Query.class);

        when(entityManager.createNativeQuery(anyString(), eq(ParcelaEntry.class))).thenReturn(queryMock);
        when(queryMock.setParameter(eq("idSimulacao"), anyLong())).thenReturn(queryMock);
        when(queryMock.getResultList()).thenReturn(Collections.emptyList());

        List<ParcelaEntry> result = simulacaoDao.getParcelas(1L);

        assertTrue(result.isEmpty());
    }
}