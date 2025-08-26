package br.gov.caixa.model.telemetria;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;


@Getter
@Setter
@Builder
public class TelemetriaResumoDto {
    private String dataReferencia;
    private List<EndpointResumoDto> listaEndpoints;
}