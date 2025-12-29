package br.com.challenge.fictcred.dto;

import br.com.challenge.fictcred.enums.StatusParcela;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PropostaCreditoListDTO {

    private Long id;
    private BigDecimal valorSolicitado;
    private int numeroParcelas;
    private StatusParcela status;
    private Date dataCriacao;
    private Long clienteId;
    private String clienteNome;
}
