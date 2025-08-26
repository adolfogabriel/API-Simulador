package br.gov.caixa.h2.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@Entity
@Table(name = "parcela")
public class ParcelaEntry {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private int numero;

    @Column(nullable = false, precision = 18, scale = 2)
    private BigDecimal valorAmortizacao;

    @Column(nullable = false, precision = 18, scale = 2)
    private BigDecimal valorJuros;

    @Column(nullable = false, precision = 18, scale = 2)
    private BigDecimal valorPrestacao;

    @ManyToOne
    @JoinColumn(name = "idResultadoSimulacao")
    private ResultadoSimulacaoEntry resultadoSimulacao;
}