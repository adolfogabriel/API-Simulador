package br.gov.caixa.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class SimulacaoResumoDto {

    private int codigoProduto;
    private String descricaoProduto;
    private double taxaMediaJuro;
    private double valorMedioPrestacao;
    private double valorTotalDesejado;
    private double valorTotalCredito;
}
