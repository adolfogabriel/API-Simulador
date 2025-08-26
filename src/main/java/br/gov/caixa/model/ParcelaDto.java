package br.gov.caixa.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ParcelaDto {

  @JsonProperty("numero")
  private Integer numero;

  @JsonProperty("valorAmortizacao")
  private Double valorAmortizacao;

  @JsonProperty("valorJuros")
  private Double valorJuros;

  @JsonProperty("valorPrestacao")
  private Double valorPrestacao;
}
