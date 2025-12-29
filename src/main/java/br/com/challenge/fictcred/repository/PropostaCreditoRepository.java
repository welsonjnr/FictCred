package br.com.challenge.fictcred.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.challenge.fictcred.model.PropostaCredito;
import br.com.challenge.fictcred.model.Cliente;
import java.util.List;

@Repository
public interface PropostaCreditoRepository extends JpaRepository<PropostaCredito, Long> {
    List<PropostaCredito> findByCliente(Cliente cliente);
}

