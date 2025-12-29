package br.com.challenge.fictcred.controller;

import br.com.challenge.fictcred.dto.ClienteInsertDTO;
import br.com.challenge.fictcred.dto.ClienteListDTO;
import br.com.challenge.fictcred.dto.ClienteUpdateDTO;
import br.com.challenge.fictcred.model.Cliente;
import br.com.challenge.fictcred.service.ClienteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Tag(name = "Cliente", description = "APIs para gerenciamento de clientes")
@RestController
@RequestMapping("fictcred/v1/api/cliente")
public class ClienteController {

    @Autowired
    private ClienteService clienteService;

    @Operation(summary = "Criar cliente",
               description = "Cria um novo cliente no sistema com os dados fornecidos.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Cliente criado com sucesso"),
        @ApiResponse(responseCode = "400", description = "Dados inválidos fornecidos")
    })
    @PostMapping
    public ResponseEntity<ClienteListDTO> criarCliente(
            @Parameter(description = "Dados do cliente a ser criado", required = true)
            @RequestBody ClienteInsertDTO clienteDTO) {
        Cliente cliente = convertToEntity(clienteDTO);
        Cliente clienteSalvo = clienteService.salvar(cliente);
        ClienteListDTO response = convertToListDTO(clienteSalvo);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Atualizar cliente",
               description = "Atualiza os dados de um cliente existente pelo seu ID único.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Cliente atualizado com sucesso"),
        @ApiResponse(responseCode = "404", description = "Cliente não encontrado")
    })
    @PutMapping("/{id}")
    public ResponseEntity<ClienteListDTO> atualizarCliente(
            @Parameter(description = "ID do cliente a ser atualizado", required = true)
            @PathVariable Long id,
            @Parameter(description = "Dados atualizados do cliente", required = true)
            @RequestBody ClienteUpdateDTO clienteDTO) {
        try {
            Cliente cliente = convertToEntity(clienteDTO);
            Cliente clienteAtualizado = clienteService.atualizar(id, cliente);
            ClienteListDTO response = convertToListDTO(clienteAtualizado);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Buscar cliente por ID",
               description = "Busca um cliente específico pelo seu ID único.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Cliente encontrado e retornado"),
        @ApiResponse(responseCode = "404", description = "Cliente não encontrado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<ClienteListDTO> buscarClientePorId(
            @Parameter(description = "ID do cliente a ser buscado", required = true)
            @PathVariable Long id) {
        return clienteService.buscarPorId(id)
                .map(cliente -> ResponseEntity.ok(convertToListDTO(cliente)))
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Listar todos os clientes",
               description = "Retorna uma lista com todos os clientes cadastrados no sistema.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de clientes retornada com sucesso")
    })
    @GetMapping
    public ResponseEntity<List<ClienteListDTO>> listarClientes() {
        List<Cliente> clientes = clienteService.listarTodos();
        List<ClienteListDTO> dtos = clientes.stream()
                .map(this::convertToListDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    @Operation(summary = "Remover cliente",
               description = "Remove um cliente do sistema pelo seu ID único.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Cliente removido com sucesso"),
        @ApiResponse(responseCode = "404", description = "Cliente não encontrado")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> removerCliente(
            @Parameter(description = "ID do cliente a ser removido", required = true)
            @PathVariable Long id) {
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
