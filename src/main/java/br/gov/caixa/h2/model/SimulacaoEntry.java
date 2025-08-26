package br.gov.caixa.h2.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "simulacao")
public class SimulacaoEntry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idSimulacao;

    @Column(nullable = false)
    private int codigoProduto;

    @Column(nullable = false)
    private String descricaoProduto;

    @Column(nullable = false, precision = 18, scale = 4)
    private BigDecimal taxaJuros;

    @Column(nullable = false, precision = 18, scale = 2)
    private BigDecimal valorDesejado;

    @Column(nullable = false)
    private LocalDate dataReferencia = LocalDate.now();

    @OneToMany(mappedBy = "simulacao", cascade = CascadeType.ALL)
    private List<ResultadoSimulacaoEntry> resultadoSimulacao;
}