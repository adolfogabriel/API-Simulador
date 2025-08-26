package br.gov.caixa.h2.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "resultado_simulacao")
public class ResultadoSimulacaoEntry {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String tipo;

    @ManyToOne
    @JoinColumn(name = "idSimulacao")
    private SimulacaoEntry simulacao;

    @OneToMany(mappedBy = "resultadoSimulacao", cascade = CascadeType.ALL)
    private List<ParcelaEntry> parcelas;
}