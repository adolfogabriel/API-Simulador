package br.gov.caixa.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EntradaSimulacaoDto {

    @JsonProperty("valorDesejado")
    @NotNull(message = "ValorDesejado não informado")
    @Min(value = 1,message = "ValorDesejado deve ser maior que 0")
    private Double valorDesejado;

    @JsonProperty("prazo")
    @NotNull(message = "Prazo não informado")
    @Min(value = 1,message = "Prazo deve ser maior que 0")
    private Integer prazo;
}
