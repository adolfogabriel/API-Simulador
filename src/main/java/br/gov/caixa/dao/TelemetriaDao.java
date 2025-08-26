package br.gov.caixa.dao;


import br.gov.caixa.h2.model.Telemetria;
import br.gov.caixa.model.SimulacaoResumoDto;
import br.gov.caixa.model.telemetria.EndpointResumoDto;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class TelemetriaDao {

    @PersistenceContext(unitName = "h2PU")
    private EntityManager entityManager;

    @Transactional("h2TransactionManager")
    public void saveSolicitacao(Telemetria telemetria) {
        try {
            entityManager.persist(telemetria);
        } catch (Exception ex) {
            log.error(ex.getMessage());
        }

    }

    public List<EndpointResumoDto> listartelemetria(String data) {
        String sql = "SELECT " +
                "    ROUND(100.0 * SUM(CASE WHEN e.HTTP_STATUS = 200 THEN 1 ELSE 0 END) / COUNT(e.ID), 2) AS percentual, " +
                "    COUNT(e.ID) AS qtd_requisicoes, " +
                "    FLOOR(AVG(e.TEMPO)) AS tempo_medio, " +
                "    MIN(e.TEMPO) AS tempo_minimo, " +
                "    MAX(e.TEMPO) AS tempo_maximo " +
                "FROM TELEMETRIA t " +
                "JOIN ENDPOINTINFO e ON e.TELEMETRIA_ID = t.ID " +
                "WHERE t.DATA_REFERENCIA = ? " +
                "GROUP BY t.DATA_REFERENCIA " +
                "ORDER BY t.DATA_REFERENCIA";

        List<Object[]> resultados = entityManager.createNativeQuery(sql)
                .setParameter(1, data)
                .getResultList();

        List<EndpointResumoDto> dtos = new ArrayList<>();
        for (Object[] row : resultados) {

            dtos.add(EndpointResumoDto.builder()
                    .nomeApi("Api_Simulador_Credito")
                    .percentualSucesso(((BigDecimal) row[0]).doubleValue())
                    .qtdRequisicoes(((Number) row[1]).intValue())
                    .tempoMedio(((Number) row[2]).intValue())
                    .tempoMinimo(((Number) row[3]).intValue())
                    .tempoMaximo(((Number) row[4]).intValue())
                    .build());
        }
        return dtos;
    }

}
