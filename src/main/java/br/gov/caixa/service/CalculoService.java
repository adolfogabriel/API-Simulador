package br.gov.caixa.service;


import br.gov.caixa.dao.ProdutoDao;
import br.gov.caixa.dao.SimulacaoDao;
import br.gov.caixa.h2.model.ParcelaEntry;
import br.gov.caixa.h2.model.ResultadoSimulacaoEntry;
import br.gov.caixa.h2.model.SimulacaoEntry;
import br.gov.caixa.kafka.KafkaProducer;
import br.gov.caixa.model.*;
import br.gov.caixa.util.Calculos;
import com.google.gson.Gson;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Slf4j
@RestController
@AllArgsConstructor
public class CalculoService {

    @Autowired
    ProdutoDao produtoDao;

    @Autowired
    SimulacaoDao simulacaoDao;

    @Autowired
    KafkaProducer kafkaProducer;

    @Autowired
    Calculos calculos;

    public ResponseEntity iniciarSimulacao(EntradaSimulacaoDto simulacaoDto) {
        try {
            SimulacaoDto simulacao = calcularPriceESac(simulacaoDto);
            if (simulacao != null) {
                salvarSimulacao(simulacao, simulacaoDto);
                return ResponseEntity.status(200).body(simulacao);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(RetornoDto.builder()
                                .mensagem("Produto não encontrado par os parâmetros informados")
                                .detalhes(null)
                                .build());
            }

        } catch (Exception e) {
            log.error("Erro ao iniciar simulacao:" + e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(RetornoDto.builder()
                            .mensagem("Erro ao iniciar simulação")
                            .detalhes(e.getMessage())
                            .build());
        }
    }

    public ResponseEntity listarSimulacoesPaginado(int pagina, int qtdRegistrosPagina) {
        try {
            return ResponseEntity.ok().body(gerarRetornoPaginada(pagina, qtdRegistrosPagina));
        } catch (Exception e) {
            log.error("Erro ao listar simulacoes paginado:" + e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(RetornoDto.builder()
                            .mensagem("Erro ao listar simulacoes paginado.")
                            .detalhes(e.getMessage())
                            .build());
        }
    }

    public ResponseEntity listarSimulacoesProdutoData(String data) {
        try {
            return ResponseEntity.ok()
                    .body(gerarRetornoValume(simulacaoDao.listarSimulacoesPorDataProduto(data), data));
        } catch (Exception e) {
            log.error("Erro ao listar simulacoes por data e produto:" + e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(RetornoDto.builder()
                            .mensagem("EErro ao listar simulacoes por data e produto.")
                            .detalhes(e.getMessage())
                            .build());
        }
    }

    private SimulacaoDto calcularPriceESac(EntradaSimulacaoDto simulacaoDto) {

        SimulacaoDto simulacao = new SimulacaoDto();
        ProdutoDto produto = obterProdutos(simulacaoDto);
        if (produto != null) {
            simulacao.setCodigoProduto(produto.getCoProduto());
            simulacao.setDescricaoProduto(produto.getNoProduto());
            simulacao.setTaxaJuros(Double.parseDouble(produto.getPcTaxaJuros()
                    .setScale(4).toString()));

            simulacao.setResultadoSimulacao(calculoPriceSac(simulacao.getTaxaJuros(),
                    simulacaoDto.getPrazo(), simulacaoDto.getValorDesejado()));
        } else {
            simulacao = null;
        }

        if (simulacao != null) {
            enviarMsg(simulacao);
        }
        return simulacao;
    }

    private ProdutoDto obterProdutos(EntradaSimulacaoDto simulacaoDto) {
        try {
            ModelMapper modelMapper = new ModelMapper();

            var produto = produtoDao.obterProduto(
                    simulacaoDto.getValorDesejado(),
                    simulacaoDto.getPrazo());
            if (produto != null) {
                return modelMapper.map(produto, ProdutoDto.class);
            } else {
                return null;
            }
        } catch (Exception e) {
            log.error("Erro ao obter produtos:" + e);
            throw new RuntimeException("Erro ao obter produtos");
        }
    }

    private List<ResultadoSimulacaoDto> calculoPriceSac(Double taxa, Integer prazo, Double valor) {

        List<ResultadoSimulacaoDto> simulacaoDtos = new ArrayList<>();

        ResultadoSimulacaoDto resultadoSimulacaoDtosPrice = new ResultadoSimulacaoDto();
        resultadoSimulacaoDtosPrice.setTipo("PRICE");
        resultadoSimulacaoDtosPrice.setParcelas(calculos.calculaTabelaPrice(valor, prazo, taxa));
        simulacaoDtos.add(resultadoSimulacaoDtosPrice);


        ResultadoSimulacaoDto resultadoSimulacaoDtosSac = new ResultadoSimulacaoDto();
        resultadoSimulacaoDtosSac.setTipo("SAC");
        resultadoSimulacaoDtosSac.setParcelas(calculos.calculaTabelaSac(valor, prazo, taxa));
        simulacaoDtos.add(resultadoSimulacaoDtosSac);

        return simulacaoDtos;
    }

    private void enviarMsg(SimulacaoDto simulacao) {
        try {
            if (simulacao != null) {
                kafkaProducer.send(new Gson().toJson(simulacao));
            }
        } catch (Exception e) {
            log.error("Erro ao enviar mensage");
            throw new RuntimeException("Erro ao enviar mensage");
        }
    }


    private void salvarSimulacao(SimulacaoDto simulacaoDto, EntradaSimulacaoDto entradaSimulacaoDto) {
        SimulacaoEntry simulacao = new SimulacaoEntry();
        simulacao.setCodigoProduto(simulacaoDto.getCodigoProduto());
        simulacao.setDescricaoProduto(simulacaoDto.getDescricaoProduto());
        simulacao.setTaxaJuros(BigDecimal.valueOf(simulacaoDto.getTaxaJuros()));
        simulacao.setValorDesejado(BigDecimal.valueOf(entradaSimulacaoDto.getValorDesejado()));


        List<ResultadoSimulacaoEntry> resultados = new ArrayList<>();
        for (ResultadoSimulacaoDto resultadoDto : simulacaoDto.getResultadoSimulacao()) {
            ResultadoSimulacaoEntry resultado = new ResultadoSimulacaoEntry();
            resultado.setTipo(resultadoDto.getTipo());
            resultado.setSimulacao(simulacao);

            List<ParcelaEntry> parcelas = new ArrayList<>();
            for (ParcelaDto parcelaDto : resultadoDto.getParcelas()) {
                ParcelaEntry parcela = new ParcelaEntry();
                parcela.setNumero(parcelaDto.getNumero());
                parcela.setValorAmortizacao(BigDecimal.valueOf(parcelaDto.getValorAmortizacao()));
                parcela.setValorJuros(BigDecimal.valueOf(parcelaDto.getValorJuros()));
                parcela.setValorPrestacao(BigDecimal.valueOf(parcelaDto.getValorPrestacao()));
                parcela.setResultadoSimulacao(resultado);
                parcelas.add(parcela);
            }
            resultado.setParcelas(parcelas);
            resultados.add(resultado);
        }
        simulacao.setResultadoSimulacao(resultados);

        simulacaoDao.saveSolicitacao(simulacao);
    }

    private SimulacaoPaginadaDto gerarRetornoPaginada(int pagina, int qtdRegistrosPagina) {
        return SimulacaoPaginadaDto.builder()
                .pagina(pagina)
                .qtdRegistros(simulacaoDao.contarTotalSimulacoes())
                .qtdRegistrosPagina(qtdRegistrosPagina)
                .registros(simulacaoDao.listarSimulacoesComTotalParcelas(pagina, qtdRegistrosPagina)).build();
    }

    private SimulacoesResumoDto gerarRetornoValume(List<SimulacaoResumoDto> resumoDtos, String data) {

        if (resumoDtos == null || resumoDtos.isEmpty()) {
            return SimulacoesResumoDto.builder()
                    .dataReferencia(data)
                    .simulacoes(Collections.emptyList())
                    .build();
        }
        return SimulacoesResumoDto.builder()
                .dataReferencia(data)
                .simulacoes(resumoDtos).build();
    }
}


