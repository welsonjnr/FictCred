package br.com.challenge.fictcred.controller;

import br.com.challenge.fictcred.dto.PropostaCreditoInsertDTO;
import br.com.challenge.fictcred.dto.PropostaCreditoListDTO;
import br.com.challenge.fictcred.model.PropostaCredito;
import br.com.challenge.fictcred.service.PropostaCreditoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("fictcred/v1/api/proposta-cliente")
public class PropostaCreditoController {

    @Autowired
    private PropostaCreditoService propostaCreditoService;

    @PostMapping("/{clienteId}")
    public ResponseEntity<PropostaCreditoListDTO> criarProposta(@PathVariable Long clienteId, @RequestBody PropostaCreditoInsertDTO propostaDTO) {
        try {
            PropostaCredito proposta = convertToEntity(propostaDTO);
            PropostaCredito propostaCriada = propostaCreditoService.criarProposta(clienteId, proposta);
            PropostaCreditoListDTO response = convertToListDTO(propostaCriada);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<PropostaCreditoListDTO> buscarPropostaPorId(@PathVariable Long id) {
        return propostaCreditoService.buscarPorId(id)
                .map(proposta -> ResponseEntity.ok(convertToListDTO(proposta)))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/cliente/{clienteId}")
    public ResponseEntity<List<PropostaCreditoListDTO>> listarPropostasPorCliente(@PathVariable Long clienteId) {
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
