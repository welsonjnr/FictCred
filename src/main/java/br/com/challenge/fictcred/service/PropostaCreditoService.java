package br.com.challenge.fictcred.service;

import br.com.challenge.fictcred.enums.StatusParcela;
import br.com.challenge.fictcred.model.Cliente;
import br.com.challenge.fictcred.model.PropostaCredito;
import br.com.challenge.fictcred.repository.ClienteRepository;
import br.com.challenge.fictcred.repository.PropostaCreditoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class PropostaCreditoService {

    @Autowired
    private PropostaCreditoRepository propostaCreditoRepository;

    @Autowired
    private ClienteRepository clienteRepository;

    public PropostaCredito criarProposta(Long clienteId, PropostaCredito proposta) {
        Optional<Cliente> clienteOpt = clienteRepository.findById(clienteId);
        if (clienteOpt.isEmpty()) {
            throw new RuntimeException("Cliente não encontrado");
        }
        Cliente cliente = clienteOpt.get();

        StatusParcela status = avaliarProposta(proposta, cliente);

        proposta.setCliente(cliente);
        proposta.setStatus(status);
        proposta.setDataCriacao(new Date());

        return propostaCreditoRepository.save(proposta);
    }

    private StatusParcela avaliarProposta(PropostaCredito proposta, Cliente cliente) {
        if (proposta.getValorSolicitado().compareTo(cliente.getRendaMensal().multiply(java.math.BigDecimal.valueOf(5))) > 0) {
            return StatusParcela.REPROVADA;
        }

        if (proposta.getNumeroParcelas() < 1 || proposta.getNumeroParcelas() > 24) {
            return StatusParcela.REPROVADA;
        }

        return StatusParcela.APROVADA;
    }

    public Optional<PropostaCredito> buscarPorId(Long id) {
        return propostaCreditoRepository.findById(id);
    }

    public List<PropostaCredito> listarPorCliente(Long clienteId) {
        Optional<Cliente> clienteOpt = clienteRepository.findById(clienteId);
        if (clienteOpt.isEmpty()) {
            throw new RuntimeException("Cliente não encontrado");
        }
        return propostaCreditoRepository.findByCliente(clienteOpt.get());
    }
}
