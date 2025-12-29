package br.com.challenge.fictcred.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class PropostaCreditoInsertDTO {

    @NotNull(message = "O valor solicitado da proposta não pode ser nulo")
    @Positive(message = "O valor solicitado da proposta deve ser maior que zero")
    private BigDecimal valorSolicitado;

    @NotNull(message = "O numero de parcelas da proposta não pode ser nulo")
    @Positive(message = "Onumero de parcelas da proposta deve ser maior que zero")
    private int numeroParcelas;

    public BigDecimal getValorSolicitado() {
        return valorSolicitado;
    }

    public void setValorSolicitado(BigDecimal valorSolicitado) {
        this.valorSolicitado = valorSolicitado;
    }

    public int getNumeroParcelas() {
        return numeroParcelas;
    }

    public void setNumeroParcelas(int numeroParcelas) {
        this.numeroParcelas = numeroParcelas;
    }
}
