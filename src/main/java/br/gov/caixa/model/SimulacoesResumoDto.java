package br.gov.caixa.model;


import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class SimulacoesResumoDto {
    private String dataReferencia;
    private List<SimulacaoResumoDto> simulacoes;
}
