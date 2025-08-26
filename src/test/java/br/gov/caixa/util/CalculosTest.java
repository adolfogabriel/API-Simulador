package br.gov.caixa.util;

import br.gov.caixa.model.ParcelaDto;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class CalculosTest {

    @Test
    void calculaPrestacao_shouldReturnZeroForZeroLoanAmount() {
        double resultado = Calculos.calculaPrestacao(0.0, 12, 0.02);
        assertEquals(0.0, resultado, 0.01);
    }

    @Test
    void calculaTabelaPrice_shouldReturnCorrectNumberOfInstallments() {
        Calculos calculos = new Calculos();
        List<ParcelaDto> parcelas = calculos.calculaTabelaPrice(10000.0, 12, 0.02);
        assertEquals(12, parcelas.size());
    }

    @Test
    void calculaTabelaPrice_shouldReturnEmptyListForZeroInstallments() {
        Calculos calculos = new Calculos();
        List<ParcelaDto> parcelas = calculos.calculaTabelaPrice(10000.0, 0, 0.02);
        assertTrue(parcelas.isEmpty());
    }

    @Test
    void calculaTabelaSac_shouldReturnCorrectNumberOfInstallments() {
        Calculos calculos = new Calculos();
        List<ParcelaDto> parcelas = calculos.calculaTabelaSac(10000.0, 12, 0.02);
        assertEquals(12, parcelas.size());
    }

    @Test
    void calculaTabelaSac_shouldReturnEmptyListForZeroInstallments() {
        Calculos calculos = new Calculos();
        List<ParcelaDto> parcelas = calculos.calculaTabelaSac(10000.0, 0, 0.02);
        assertTrue(parcelas.isEmpty());
    }

    @Test
    void calcularParcelas_shouldReturnEqualInstallmentsForValidInputs() {
        double[] parcelas = Calculos.calcularParcelas(12000.0, 12);
        assertEquals(12, parcelas.length);
        assertEquals(1000.0, parcelas[0], 0.01);
    }

    @Test
    void calcularSaldosDevedor_shouldReturnCorrectBalances() {
        double[] parcelas = {1000.0, 1000.0, 1000.0};
        double[] saldos = Calculos.calcularSaldosDevedor(3000.0, parcelas);
        assertEquals(3000.0, saldos[0], 0.01);
        assertEquals(2000.0, saldos[1], 0.01);
        assertEquals(1000.0, saldos[2], 0.01);
    }
}