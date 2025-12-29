package br.com.challenge.fictcred.controller;

import br.com.challenge.fictcred.dto.PropostaCreditoInsertDTO;
import br.com.challenge.fictcred.dto.PropostaCreditoListDTO;
import br.com.challenge.fictcred.model.PropostaCredito;
import br.com.challenge.fictcred.service.PropostaCreditoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("fictcred/v1/api/proposta-cliente")
@Tag(name = "Proposta de Crédito", description = "API para gerenciamento de propostas de crédito")
public class PropostaCreditoController {

    @Autowired
    private PropostaCreditoService propostaCreditoService;

    @Operation(summary = "Criar proposta de crédito",
               description = "Cria uma nova proposta de crédito para um cliente específico. A proposta é avaliada automaticamente com base nas regras de negócio (valor solicitado não pode exceder 5x a renda mensal do cliente, número de parcelas deve estar entre 1 e 24).")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Proposta criada com sucesso"),
        @ApiResponse(responseCode = "400", description = "Cliente não encontrado ou dados inválidos")
    })
    @PostMapping("/{clienteId}")
    public ResponseEntity<PropostaCreditoListDTO> criarProposta(
            @Parameter(description = "ID do cliente para o qual a proposta será criada", required = true)
            @PathVariable Long clienteId,
            @Parameter(description = "Dados da proposta a ser criada", required = true)
            @Valid @RequestBody PropostaCreditoInsertDTO propostaDTO) {
        try {
            PropostaCredito proposta = convertToEntity(propostaDTO);
            PropostaCredito propostaCriada = propostaCreditoService.criarProposta(clienteId, proposta);
            PropostaCreditoListDTO response = convertToListDTO(propostaCriada);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @Operation(summary = "Buscar proposta por id",
               description = "Retorna a proposta de acordo com o id passado")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Proposta encontrada e retornada com sucesso"),
        @ApiResponse(responseCode = "404", description = "Proposta não encontrado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<PropostaCreditoListDTO> buscarPropostaPorId(@PathVariable Long id) {
        return propostaCreditoService.buscarPorId(id)
                .map(proposta -> ResponseEntity.ok(convertToListDTO(proposta)))
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Listar propostas por cliente",
               description = "Lista todas as propostas de crédito associadas a um cliente específico.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de propostas retornada com sucesso"),
        @ApiResponse(responseCode = "404", description = "Cliente não encontrado")
    })
    @GetMapping("/cliente/{clienteId}")
    public ResponseEntity<List<PropostaCreditoListDTO>> listarPropostasPorCliente(
            @Parameter(description = "ID do cliente cujas propostas serão listadas", required = true)
            @PathVariable Long clienteId) {
        try {
            List<PropostaCredito> propostas = propostaCreditoService.listarPorCliente(clienteId);
            List<PropostaCreditoListDTO> dtos = propostas.stream()
                    .map(this::convertToListDTO)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(dtos);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    private PropostaCredito convertToEntity(PropostaCreditoInsertDTO dto) {
        PropostaCredito proposta = new PropostaCredito();
        proposta.setValorSolicitado(dto.getValorSolicitado());
        proposta.setNumeroParcelas(dto.getNumeroParcelas());
        return proposta;
    }

    private PropostaCreditoListDTO convertToListDTO(PropostaCredito proposta) {
        return new PropostaCreditoListDTO(
                proposta.getId(),
                proposta.getValorSolicitado(),
                proposta.getNumeroParcelas(),
                proposta.getStatus(),
                proposta.getDataCriacao(),
                proposta.getCliente().getId(),
                proposta.getCliente().getNome()
        );
    }
}
