package br.gov.caixa.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;


import java.math.BigDecimal;


@Getter
@Setter
@Entity
@Table(name = "PRODUTO", schema = "dbo", catalog = "hack")
public class ProdutoEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "CO_PRODUTO")
    private int coProduto;
    @Basic
    @Column(name = "NO_PRODUTO")
    private String noProduto;
    @Basic
    @Column(name = "PC_TAXA_JUROS")
    private BigDecimal pcTaxaJuros;
    @Basic
    @Column(name = "NU_MINIMO_MESES")
    private short nuMinimoMeses;
    @Basic
    @Column(name = "NU_MAXIMO_MESES")
    private Short nuMaximoMeses;
    @Basic
    @Column(name = "VR_MINIMO")
    private BigDecimal vrMinimo;
    @Basic
    @Column(name = "VR_MAXIMO")
    private BigDecimal vrMaximo;
}
