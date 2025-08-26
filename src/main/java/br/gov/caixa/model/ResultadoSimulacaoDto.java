package br.gov.caixa.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import org.springframework.validation.annotation.Validated;

import java.util.List;


@Validated
@Getter
@Setter
public class ResultadoSimulacaoDto {
  @JsonProperty("tipo")
  private String tipo;

  @JsonProperty("parcelas")
  private List<ParcelaDto> parcelas;

}
