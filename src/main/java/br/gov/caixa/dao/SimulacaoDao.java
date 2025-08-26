package br.gov.caixa.dao;


import br.gov.caixa.h2.model.ParcelaEntry;
import br.gov.caixa.h2.model.SimulacaoEntry;
import br.gov.caixa.model.SimulacaoRegistroDto;
import br.gov.caixa.model.SimulacaoResumoDto;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


@Service
@Slf4j
public class SimulacaoDao {


    @PersistenceContext(unitName = "h2PU")
    private EntityManager entityManager;

    @Transactional("h2TransactionManager")
    public void saveSolicitacao(SimulacaoEntry simulacaoEntry) {
        try {
            entityManager.persist(simulacaoEntry);
        } catch (Exception ex) {
            log.error(ex.getMessage());
        }

    }

    public List<SimulacaoRegistroDto> listarSimulacoesComTotalParcelas(int pagina, int qtdRegistrosPagina) {
        String sql = "SELECT simula.ID_SIMULACAO, " +
                "       COUNT(parcela.ID)            AS qtdeParcelas, " +
                "       SUM(parcela.VALOR_PRESTACAO) AS totalParcelas, " +
                "       simula.VALOR_DESEJADO " +
                "FROM SIMULACAO simula " +
                "         JOIN RESULTADO_SIMULACAO resultado ON resultado.ID_SIMULACAO = simula.ID_SIMULACAO " +
                "         JOIN PARCELA parcela ON parcela.ID_RESULTADO_SIMULACAO = resultado.ID " +
                "GROUP BY simula.ID_SIMULACAO " +
                "ORDER BY simula.ID_SIMULACAO " +
                "LIMIT ? OFFSET ?";

        int offset = Math.max(0, (pagina - 1) * qtdRegistrosPagina);
        List<Object[]> resultados = entityManager.createNativeQuery(sql)
                .setParameter(1, qtdRegistrosPagina)
                .setParameter(2, offset)
                .getResultList();
        List<SimulacaoRegistroDto> dtos = new ArrayList<>();
        for (Object[] row : resultados) {
            dtos.add(SimulacaoRegistroDto.builder()
                    .idSimulacao(((Number) row[0]).longValue())
                    .prazo(((Number) row[1]).intValue())
                    .valorTotalParcelas(((BigDecimal) row[2]).doubleValue())
                    .valorDesejado(((BigDecimal) row[3]).setScale(2, RoundingMode.HALF_UP).doubleValue()).build());
        }
        return dtos;
    }

    public List<SimulacaoResumoDto> listarSimulacoesPorDataProduto(String data) {
        String sql = "SELECT " +
                "    simula.CODIGO_PRODUTO, " +
                "    ROUND(AVG(simula.TAXA_JUROS), 4) AS taxaMediaJuro, " +
                "    ROUND(SUM(simula.VALOR_DESEJADO), 2) AS valorTotalDesejado, " +
                "    ROUND(AVG( " +
                "        (SELECT SUM(parcela.VALOR_PRESTACAO) " +
                "         FROM RESULTADO_SIMULACAO resultado " +
                "         JOIN PARCELA parcela ON parcela.ID_RESULTADO_SIMULACAO = resultado.ID " +
                "         WHERE resultado.ID_SIMULACAO = simula.ID_SIMULACAO) " +
                "    ), 2) AS valorMedioPrestacao, " +
                "    ROUND(" +
                "        (SELECT SUM(parcela.VALOR_AMORTIZACAO) " +
                "         FROM RESULTADO_SIMULACAO resultado " +
                "         JOIN PARCELA parcela ON parcela.ID_RESULTADO_SIMULACAO = resultado.ID " +
                "         WHERE resultado.ID_SIMULACAO IN (" +
                "             SELECT s2.ID_SIMULACAO FROM SIMULACAO s2 WHERE s2.CODIGO_PRODUTO = simula.CODIGO_PRODUTO " +
                "         )" +
                "        ), 2" +
                "    ) AS valorTotalCredito, " +
                "    MIN(simula.DESCRICAO_PRODUTO) AS descricaoProduto " +
                "FROM SIMULACAO simula " +
                "WHERE simula.DATA_REFERENCIA = ? " +
                "GROUP BY simula.CODIGO_PRODUTO " +
                "ORDER BY simula.CODIGO_PRODUTO";

        List<Object[]> resultados = entityManager.createNativeQuery(sql)
                .setParameter(1, data)
                .getResultList();
        List<SimulacaoResumoDto> dtos = new ArrayList<>();
        for (Object[] row : resultados) {
            dtos.add(SimulacaoResumoDto.builder()
                    .codigoProduto(((Number) row[0]).intValue())
                    .taxaMediaJuro(((BigDecimal) row[1]).doubleValue())
                    .valorTotalDesejado(((BigDecimal) row[2]).doubleValue())
                    .valorMedioPrestacao(((BigDecimal) row[3]).doubleValue())
                    .valorTotalCredito(((BigDecimal) row[4]).doubleValue())
                    .descricaoProduto((String) row[5])
                    .build());


        }
        return dtos;
    }

    public int contarTotalSimulacoes() {
        String sql = "SELECT COUNT(*) FROM SIMULACAO";
        Object resultado = entityManager.createNativeQuery(sql).getSingleResult();
        return ((Number) resultado).intValue();
    }

    public List<ParcelaEntry> getParcelas(long idSimulacao) {
        String sql = "SELECT * FROM PARCELA WHERE ID_RESULTADO_SIMULACAO IN " +
                "(SELECT ID FROM RESULTADO_SIMULACAO WHERE ID_SIMULACAO = :idSimulacao) " +
                "ORDER BY NUMERO";
        List<ParcelaEntry> parcelas = entityManager.createNativeQuery(sql, ParcelaEntry.class)
                .setParameter("idSimulacao", idSimulacao)
                .getResultList();
        return parcelas.isEmpty() ? Collections.emptyList() : parcelas;
    }
}
