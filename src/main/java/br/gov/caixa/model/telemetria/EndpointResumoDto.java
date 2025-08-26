package br.gov.caixa.model.telemetria;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class EndpointResumoDto {
    private String nomeApi;
    private int qtdRequisicoes;
    private int tempoMedio;
    private int tempoMinimo;
    private int tempoMaximo;
    private double percentualSucesso;
}