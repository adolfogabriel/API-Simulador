package br.gov.caixa.model;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
public class RetornoDto {

    @JsonProperty("Timestamp")
    @Builder.Default
    private LocalDateTime timestamp = LocalDateTime.now();

    @JsonProperty("Mensagem")
    private String mensagem;

    @JsonProperty("Violacoes")
    List<String> violacoes;

    @JsonProperty("Detalhes")
    String detalhes;
}
