package br.gov.caixa.h2.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "endpointinfo")
public class EndpointInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String metodo;
    private long tempo;
    private int httpStatus;

    @ManyToOne
    @JoinColumn(name = "telemetria_id")
    private Telemetria telemetria;

}