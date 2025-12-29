package br.com.challenge.fictcred.service;

import br.com.challenge.fictcred.model.Cliente;
import br.com.challenge.fictcred.repository.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ClienteService {

    @Autowired
    private ClienteRepository clienteRepository;

    public Cliente salvar(Cliente cliente) {
        return clienteRepository.save(cliente);
    }

    public Optional<Cliente> buscarPorId(Long id) {
        return clienteRepository.findById(id);
    }

    public List<Cliente> listarTodos() {
        return clienteRepository.findAll();
    }

    public Cliente atualizar(Long id, Cliente clienteAtualizado) {
        Optional<Cliente> clienteExistente = clienteRepository.findById(id);
        if (clienteExistente.isPresent()) {
            Cliente cliente = clienteExistente.get();
            cliente.setNome(clienteAtualizado.getNome());
            cliente.setCpf(clienteAtualizado.getCpf());
            cliente.setRendaMensal(clienteAtualizado.getRendaMensal());
            cliente.setDataCadastro(clienteAtualizado.getDataCadastro());
            return clienteRepository.save(cliente);
        } else {
            throw new RuntimeException("Cliente não encontrado com id: " + id);
        }
    }

    public void deletar(Long id) {
        clienteRepository.deleteById(id);
    }
}
