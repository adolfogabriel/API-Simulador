package br.gov.caixa.util;


import br.gov.caixa.model.ParcelaDto;
import br.gov.caixa.model.ResultadoSimulacaoDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Slf4j
@Component
public class Calculos {
    public static double calculaPrestacao(double ValContr, int NumPrest, double TaxaJuros) {

        TaxaJuros = TaxaJuros * 100;
        TaxaJuros = TaxaJuros / 100;
        double I2 = 1 + TaxaJuros;
        int dif = (int) ((new Date().getTime() - new Date().getTime()) / 86400000);
        double N2 = (double) dif / 30;
        double N = NumPrest + N2;
        double N3 = Math.pow(I2, NumPrest) - 1;
        double N4 = N / N3;
        double ValPrest = Math.pow(I2, N4);
        ValPrest = ValContr * (TaxaJuros * Math.pow(I2, N4));
        ValPrest = ValContr * (TaxaJuros * (Math.pow(I2, N) / N3));
        ValPrest = Math.rint(ValPrest * 100) / 100;
        return ValPrest;
    }

    public List<ParcelaDto> calculaTabelaPrice(double ValContr, int NumPrest, double TaxaJuros) {

        List<ParcelaDto> parcelaDtoList = new ArrayList<>();

        int quant = 1;
        double valorEmprestimo = ValContr;
        int numeroParcelas = NumPrest;
        double taxaJuros = TaxaJuros;

        double valorParcela = calculaPrestacao(valorEmprestimo, numeroParcelas, taxaJuros);

        double saldoDevedor = valorEmprestimo;
        for (int i = 1; i <= numeroParcelas; i++) {
            ParcelaDto parcelaDto = new ParcelaDto();
            parcelaDto.setNumero(quant);
            parcelaDto.setValorJuros(formatarDouble((saldoDevedor * taxaJuros)));
            parcelaDto.setValorAmortizacao(formatarDouble(valorParcela - parcelaDto.getValorJuros()));
            parcelaDto.setValorPrestacao(formatarDouble(valorParcela));
            saldoDevedor -= parcelaDto.getValorAmortizacao();
            parcelaDtoList.add(parcelaDto);
            quant++;
        }
        return parcelaDtoList;
    }

    public List<ParcelaDto> calculaTabelaSac(double ValContr, int NumPrest, double TaxaJuros) {

        List<ParcelaDto> parcelaDtoList = new ArrayList<>();

        int quant = 1;
        double valorEmprestimo = ValContr;
        int numeroParcelas = NumPrest;
        double taxaJuros = TaxaJuros;

        double[] parcelas = calcularParcelas(valorEmprestimo, numeroParcelas);
        double[] saldosDevedor = calcularSaldosDevedor(valorEmprestimo, parcelas);

        for (int i = 0; i < numeroParcelas; i++) {
            ParcelaDto parcelaDto = new ParcelaDto();
            parcelaDto.setNumero(quant);
            parcelaDto.setValorJuros(formatarDouble(saldosDevedor[i] * taxaJuros));
            parcelaDto.setValorAmortizacao(formatarDouble(parcelas[i]));
            parcelaDto.setValorPrestacao(formatarDouble(parcelas[i] + parcelaDto.getValorJuros()));
            parcelaDtoList.add(parcelaDto);
            quant++;
        }

        return parcelaDtoList;
    }

    public static double[] calcularParcelas(double valorEmprestimo, int numeroParcelas) {
        double[] parcelas = new double[numeroParcelas];
        double valorParcela = valorEmprestimo / numeroParcelas;

        for (int i = 0; i < numeroParcelas; i++) {
            parcelas[i] = valorParcela;
        }

        return parcelas;
    }

    public static double[] calcularSaldosDevedor(double valorEmprestimo, double[] parcelas) {
        double[] saldosDevedor = new double[parcelas.length];
        double saldoDevedor = valorEmprestimo;

        for (int i = 0; i < parcelas.length; i++) {
            saldosDevedor[i] = saldoDevedor;
            saldoDevedor -= parcelas[i];
        }

        return saldosDevedor;
    }

    private double formatarDouble(double valor) {
        return new BigDecimal(valor)
                .setScale(2, RoundingMode.HALF_UP).doubleValue();
    }
}
