package br.com.challenge.fictcred.controller;

import br.com.challenge.fictcred.dto.ClienteInsertDTO;
import br.com.challenge.fictcred.dto.ClienteListDTO;
import br.com.challenge.fictcred.dto.ClienteUpdateDTO;
import br.com.challenge.fictcred.model.Cliente;
import br.com.challenge.fictcred.service.ClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("fictcred/v1/api/cliente")
public class ClienteController {

    @Autowired
    private ClienteService clienteService;

    @PostMapping
    public ResponseEntity<ClienteListDTO> criarCliente(@RequestBody ClienteInsertDTO clienteDTO) {
        Cliente cliente = convertToEntity(clienteDTO);
        Cliente clienteSalvo = clienteService.salvar(cliente);
        ClienteListDTO response = convertToListDTO(clienteSalvo);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ClienteListDTO> atualizarCliente(@PathVariable Long id, @RequestBody ClienteUpdateDTO clienteDTO) {
        try {
            Cliente cliente = convertToEntity(clienteDTO);
            Cliente clienteAtualizado = clienteService.atualizar(id, cliente);
            ClienteListDTO response = convertToListDTO(clienteAtualizado);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClienteListDTO> buscarClientePorId(@PathVariable Long id) {
        return clienteService.buscarPorId(id)
                .map(cliente -> ResponseEntity.ok(convertToListDTO(cliente)))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<ClienteListDTO>> listarClientes() {
        List<Cliente> clientes = clienteService.listarTodos();
        List<ClienteListDTO> dtos = clientes.stream()
                .map(this::convertToListDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> removerCliente(@PathVariable Long id) {
        clienteService.deletar(id);
        return ResponseEntity.noContent().build();
    }

    private Cliente convertToEntity(ClienteInsertDTO dto) {
        Cliente cliente = new Cliente();
        cliente.setNome(dto.getNome());
        cliente.setCpf(dto.getCpf());
        cliente.setRendaMensal(dto.getRendaMensal());
        cliente.setDataCadastro(new Date());
        return cliente;
    }

    private Cliente convertToEntity(ClienteUpdateDTO dto) {
        Cliente cliente = new Cliente();
        cliente.setNome(dto.getNome());
        cliente.setCpf(dto.getCpf());
        cliente.setRendaMensal(dto.getRendaMensal());
        cliente.setDataCadastro(dto.getDataCadastro());
        return cliente;
    }

    private ClienteListDTO convertToListDTO(Cliente cliente) {
        return new ClienteListDTO(
                cliente.getId(),
                cliente.getNome(),
                cliente.getCpf(),
                cliente.getRendaMensal(),
                cliente.getDataCadastro()
        );
    }
}
