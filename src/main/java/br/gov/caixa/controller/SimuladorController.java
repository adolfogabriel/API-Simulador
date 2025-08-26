package br.gov.caixa.controller;


import br.gov.caixa.model.*;
import br.gov.caixa.service.CalculoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


@RestController
@AllArgsConstructor
@RequestMapping("/api/v1")
@Tag(name = "Simulador", description = "Simulador de de credito SAC e Price")
@Validated
public class SimuladorController {
    @Autowired
    CalculoService service;

    @Operation(summary = "Simulador Credito")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successfully",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ResultadoSimulacaoDto.class))}),
            @ApiResponse(responseCode = "400", description = "Bad request",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = RetornoDto.class))}),
            @ApiResponse(responseCode = "500", description = "Internal Server Error",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = RetornoDto.class))})
            ,})
    @PostMapping("/Simulacao")
    public ResponseEntity iniciarSimulacao(@Valid @RequestBody EntradaSimulacaoDto simulacaoDto) {
        return service.iniciarSimulacao(simulacaoDto);
    }

    @Operation(summary = "Listar Simulações de Credito")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successfully",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = SimulacaoPaginadaDto.class))}),
            @ApiResponse(responseCode = "400", description = "Bad request",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = RetornoDto.class))}),
            @ApiResponse(responseCode = "500", description = "Internal Server Error",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = RetornoDto.class))})
            ,})
    @GetMapping("/listarSimulacoes")
    public ResponseEntity listarSimulacoes(
            @RequestParam @NotNull(message = "Página não informada") @Min(value = 1, message = "Página deve ser maior que 0") int pagina,
            @RequestParam @NotNull(message = "Qtd. registros não informada") @Min(value = 1, message = "Qtd. registros deve ser maior que 0") int qtdRegistrosPagina) {
        return service.listarSimulacoesPaginado(pagina, qtdRegistrosPagina);
    }

    @Operation(summary = "Listar Simulações de Credito por produto e por dia")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successfully",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = SimulacoesResumoDto.class))}),
            @ApiResponse(responseCode = "400", description = "Bad request",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = RetornoDto.class))}),
            @ApiResponse(responseCode = "500", description = "Internal Server Error",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = RetornoDto.class))})
            ,})
    @GetMapping("/volumesimulado/{data}")
    public ResponseEntity listarSimulacoesProdutoData(
            @Parameter(description = "Data de referência no formato yyyy-MM-dd", example = "2025-07-30", required = true)
            @Pattern(regexp = "\\d{4}-\\d{2}-\\d{2}", message = "Data deve estar no formato yyyy-MM-dd")
            @NotNull(message = "Data não informada")
            @PathVariable("data") String data) {
        return service.listarSimulacoesProdutoData(data);
    }
}

