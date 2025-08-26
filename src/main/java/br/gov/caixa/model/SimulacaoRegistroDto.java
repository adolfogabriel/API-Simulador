package br.gov.caixa.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class SimulacaoRegistroDto {

    private long idSimulacao;
    private double valorDesejado;
    private int prazo;
    private double valorTotalParcelas;
}
