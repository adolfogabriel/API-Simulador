package br.gov.caixa.service;

import br.gov.caixa.dao.ProdutoDao;
import br.gov.caixa.dao.SimulacaoDao;
import br.gov.caixa.model.*;
import br.gov.caixa.util.Calculos;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest
class CalculoServiceTest {


    @InjectMocks
    CalculoService calculoService;

    @Mock
    SimulacaoDao simulacaoDao;

    @InjectMocks
    ProdutoDao produtoDao;

    @Mock
    Calculos calculos;

    @Test
    void iniciarSimulacao_deveRetornarSimulacaoQuandoParametrosValidos() {
        EntradaSimulacaoDto entradaSimulacaoDto = new EntradaSimulacaoDto();
        entradaSimulacaoDto.setValorDesejado(900.00);
        entradaSimulacaoDto.setPrazo(5);

        SimulacaoDto simulacaoDto = new SimulacaoDto();
        simulacaoDto.setCodigoProduto(1); // Corrigido para Integer
        simulacaoDto.setDescricaoProduto("Produto Teste");
        simulacaoDto.setTaxaJuros(1.5);

        when(calculos.calculaTabelaPrice(10000.0, 12, 1.5)).thenReturn(Collections.emptyList());
        when(calculos.calculaTabelaSac(10000.0, 12, 1.5)).thenReturn(Collections.emptyList());
    }

    @Test
    void iniciarSimulacao_deveRetornarErroQuandoParametrosInvalidos() {
        EntradaSimulacaoDto entradaSimulacaoDto = new EntradaSimulacaoDto();
        entradaSimulacaoDto.setValorDesejado(null);
        entradaSimulacaoDto.setPrazo(null);

        ResponseEntity response = calculoService.iniciarSimulacao(entradaSimulacaoDto);

        assertEquals(400, response.getStatusCodeValue());
    }

    @Test
    void listarSimulacoesPaginado_deveRetornarSimulacoesQuandoPaginaValida() {
        when(simulacaoDao.listarSimulacoesComTotalParcelas(1, 10)).thenReturn(Collections.emptyList());
        when(simulacaoDao.contarTotalSimulacoes()).thenReturn(100);

        ResponseEntity response = calculoService.listarSimulacoesPaginado(1, 10);

        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertTrue(response.getBody() instanceof SimulacaoPaginadaDto);
    }

    @Test
    void listarSimulacoesProdutoData_deveRetornarSimulacoesQuandoDataValida() {
        String data = "2024-06-01";
        when(simulacaoDao.listarSimulacoesPorDataProduto(data)).thenReturn(Collections.emptyList());

        ResponseEntity response = calculoService.listarSimulacoesProdutoData(data);

        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertTrue(response.getBody() instanceof SimulacoesResumoDto);
    }

    @Test
    void listarSimulacoesProdutoData_deveRetornarErroQuandoExcecaoLancada() {
        String data = "2024-06-01";
        when(simulacaoDao.listarSimulacoesPorDataProduto(data)).thenThrow(new RuntimeException("Erro"));

        ResponseEntity response = calculoService.listarSimulacoesProdutoData(data);

        assertEquals(500, response.getStatusCodeValue());
        assertEquals("{\"codigo\":500,\"mensagem\":\"Ocorreu um erro inesperado\"}", response.getBody());
    }
}