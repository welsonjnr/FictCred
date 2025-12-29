package br.com.challenge.fictcred.dto;

import java.math.BigDecimal;
import java.util.Date;

public class ClienteListDTO {
    private Long id;
    private String nome;
    private String cpf;
    private BigDecimal rendaMensal;
    private Date dataCadastro;

    public ClienteListDTO(Long id, String nome, String cpf, BigDecimal rendaMensal, Date dataCadastro) {
        this.id = id;
        this.nome = nome;
        this.cpf = cpf;
        this.rendaMensal = rendaMensal;
        this.dataCadastro = dataCadastro;
    }

    public Long getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getCpf() {
        return cpf;
    }

    public BigDecimal getRendaMensal() {
        return rendaMensal;
    }

    public Date getDataCadastro() {
        return dataCadastro;
    }
}
