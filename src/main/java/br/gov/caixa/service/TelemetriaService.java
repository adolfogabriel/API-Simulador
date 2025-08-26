package br.gov.caixa.service;


import br.gov.caixa.dao.TelemetriaDao;
import br.gov.caixa.h2.model.Telemetria;
import br.gov.caixa.model.telemetria.EndpointResumoDto;
import br.gov.caixa.model.telemetria.TelemetriaResumoDto;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@AllArgsConstructor
public class TelemetriaService {


    @Autowired
    TelemetriaDao telemetriaDao;


    public ResponseEntity listarDadosTelemetria(String data) {
        try {
            return ResponseEntity.ok()
                    .body(retornoTelemetria(telemetriaDao.listartelemetria(data), data));
        } catch (Exception ex) {
            log.error("Erro ao listar dados de telemetria: " + ex.getMessage());
            return ResponseEntity.status(500).body("Erro ao listar dados de telemetria");
        }
    }

    private TelemetriaResumoDto retornoTelemetria(List<EndpointResumoDto> resumoDto, String data) {
        return TelemetriaResumoDto.builder()
                .dataReferencia(data)
                .listaEndpoints(resumoDto)
                .build();
    }
}
