package br.com.challenge.fictcred.model;

import br.com.challenge.fictcred.enums.StatusParcela;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;


@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PropostaCredito {

    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private BigDecimal valorSolicitado;
    private int numeroParcelas;
    private StatusParcela status;
    private Date dataCriacao;

    private Cliente cliente;

}
