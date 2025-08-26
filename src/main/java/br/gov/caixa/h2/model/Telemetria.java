package br.gov.caixa.h2.model;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "telemetria")
public class Telemetria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate dataReferencia = LocalDate.now();

    @OneToMany(mappedBy = "telemetria", cascade = CascadeType.ALL)
    private List<EndpointInfo> listaEndpoints;

}