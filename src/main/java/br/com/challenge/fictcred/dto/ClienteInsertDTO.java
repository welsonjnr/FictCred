package br.com.challenge.fictcred.dto;

import java.math.BigDecimal;

import org.hibernate.validator.constraints.br.CPF;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public class ClienteInsertDTO {
    
    @NotBlank(message = "Nome do cliente é obrigatório")
    @Size(min = 2, max = 100)
    private String nome;
    
    @NotBlank(message = "CPF é obrigatório")
    @CPF(message = "CPF inválido")
    private String cpf;
    
    @NotNull(message = "O valor da proposta não pode ser nulo")
    @Positive(message = "O valor da proposta ser maior que zero")
    private BigDecimal valor;

    @NotNull(message = "O valor da renda mensal não pode ser nulo")
    @Positive(message = "O valor da renda mensal deve ser maior que zero")
    private BigDecimal rendaMensal;

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public BigDecimal getRendaMensal() {
        return rendaMensal;
    }

    public void setRendaMensal(BigDecimal rendaMensal) {
        this.rendaMensal = rendaMensal;
    }


}
