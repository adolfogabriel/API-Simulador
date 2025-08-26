package br.gov.caixa.controller;


import br.gov.caixa.h2.model.Telemetria;
import br.gov.caixa.model.RetornoDto;
import br.gov.caixa.model.SimulacoesResumoDto;
import br.gov.caixa.service.TelemetriaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Telemetria", description = "Telemetria api Simulador de de credito SAC e Price")
@Validated
public class TelemetriaController {

    @Autowired
    TelemetriaService service;


    @Operation(summary = "Listar Simulações de Credito por produto e por dia")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successfully",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Telemetria.class))}),
            @ApiResponse(responseCode = "400", description = "Bad request",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = RetornoDto.class))}),
            @ApiResponse(responseCode = "500", description = "Internal Server Error",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = RetornoDto.class))})
            ,})
    @GetMapping("/telemetria/{data}")
    public ResponseEntity listarTelemetriaData(
            @Parameter(description = "Data de referência no formato yyyy-MM-dd", example = "2025-07-30", required = true)
            @Pattern(regexp = "\\d{4}-\\d{2}-\\d{2}", message = "Data deve estar no formato yyyy-MM-dd")
            @NotNull(message = "Data não informada")
            @PathVariable ("data") String data) {
        return service.listarDadosTelemetria(data);
    }

}

