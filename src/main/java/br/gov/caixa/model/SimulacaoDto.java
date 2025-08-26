package br.gov.caixa.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class SimulacaoDto {
  @JsonProperty("codigoProduto")
  private Integer codigoProduto;

  @JsonProperty("descricaoProduto")
  private String descricaoProduto;

  @JsonProperty("taxaJuros")
  private Double taxaJuros;

  @JsonProperty("resultadoSimulacao")
  private List<ResultadoSimulacaoDto> resultadoSimulacao;
}
