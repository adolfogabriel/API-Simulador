package br.gov.caixa.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class SimulacaoPaginadaDto {

    private int pagina;
    private int qtdRegistros;
    private int qtdRegistrosPagina;
    private List<SimulacaoRegistroDto> registros;
}
