package br.gov.caixa.model;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class ProdutoDto {

    @JsonProperty("codigo")
    private int coProduto;

    @JsonProperty("descricao")
    private String noProduto;

    @JsonProperty("taxa_juros")
    private BigDecimal pcTaxaJuros;

    @JsonProperty("minimo_meses")
    private short nuMinimoMeses;

    @JsonProperty("maximo_meses")
    private Short nuMaximoMeses;

    @JsonProperty("valor_minimo")
    private BigDecimal vrMinimo;

    @JsonProperty("valor_maximo")
    private BigDecimal vrMaximo;
}
