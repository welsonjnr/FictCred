package br.com.challenge.fictcred.controller;

import br.com.challenge.fictcred.dto.PropostaCreditoInsertDTO;
import br.com.challenge.fictcred.dto.PropostaCreditoListDTO;
import br.com.challenge.fictcred.enums.StatusParcela;
import br.com.challenge.fictcred.model.Cliente;
import br.com.challenge.fictcred.model.PropostaCredito;
import br.com.challenge.fictcred.service.PropostaCreditoService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class PropostaCreditoControllerTest {

    @Mock
    private PropostaCreditoService propostaCreditoService;

    @InjectMocks
    private PropostaCreditoController propostaCreditoController;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    private PropostaCredito propostaCredito;
    private PropostaCreditoInsertDTO propostaCreditoInsertDTO;
    private PropostaCreditoListDTO propostaCreditoListDTO;
    private Cliente cliente;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(propostaCreditoController).build();
        objectMapper = new ObjectMapper();

        cliente = new Cliente();
        cliente.setId(1L);
        cliente.setNome("João Silva");
        cliente.setCpf("12345678900");
        cliente.setRendaMensal(new BigDecimal("5000.00"));
        cliente.setDataCadastro(new Date());

        propostaCredito = new PropostaCredito();
        propostaCredito.setId(1L);
        propostaCredito.setValorSolicitado(new BigDecimal("10000.00"));
        propostaCredito.setNumeroParcelas(12);
        propostaCredito.setStatus(StatusParcela.APROVADA);
        propostaCredito.setDataCriacao(new Date());
        propostaCredito.setCliente(cliente);

        propostaCreditoInsertDTO = new PropostaCreditoInsertDTO();
        propostaCreditoInsertDTO.setValorSolicitado(new BigDecimal("10000.00"));
        propostaCreditoInsertDTO.setNumeroParcelas(12);

        propostaCreditoListDTO = new PropostaCreditoListDTO(1L, new BigDecimal("10000.00"), 12, StatusParcela.APROVADA, new Date(), 1L, "João Silva");
    }

    @Test
    void criarProposta_DeveRetornarPropostaCriada() throws Exception {
        when(propostaCreditoService.criarProposta(eq(1L), any(PropostaCredito.class))).thenReturn(propostaCredito);

        mockMvc.perform(post("/fictcred/v1/api/proposta-cliente/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(propostaCreditoInsertDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.valorSolicitado").value(10000.00))
                .andExpect(jsonPath("$.numeroParcelas").value(12))
                .andExpect(jsonPath("$.status").value("APROVADA"))
                .andExpect(jsonPath("$.clienteId").value(1L))
                .andExpect(jsonPath("$.clienteNome").value("João Silva"));

        verify(propostaCreditoService, times(1)).criarProposta(eq(1L), any(PropostaCredito.class));
    }

    @Test
    void listarPropostasPorCliente_DeveRetornarListaDePropostas() throws Exception {
        List<PropostaCredito> propostas = Arrays.asList(propostaCredito);
        when(propostaCreditoService.listarPorCliente(1L)).thenReturn(propostas);

        mockMvc.perform(get("/fictcred/v1/api/proposta-cliente/cliente/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].valorSolicitado").value(10000.00))
                .andExpect(jsonPath("$[0].numeroParcelas").value(12))
                .andExpect(jsonPath("$[0].status").value("APROVADA"))
                .andExpect(jsonPath("$[0].clienteId").value(1L))
                .andExpect(jsonPath("$[0].clienteNome").value("João Silva"));

        verify(propostaCreditoService, times(1)).listarPorCliente(1L);
    }

    @Test
    void buscarPropostaPorId_DeveRetornarProposta() throws Exception {
        when(propostaCreditoService.buscarPorId(1L)).thenReturn(Optional.of(propostaCredito));

        mockMvc.perform(get("/fictcred/v1/api/proposta-cliente/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.valorSolicitado").value(10000.00))
                .andExpect(jsonPath("$.numeroParcelas").value(12))
                .andExpect(jsonPath("$.status").value("APROVADA"))
                .andExpect(jsonPath("$.clienteId").value(1L))
                .andExpect(jsonPath("$.clienteNome").value("João Silva"));

        verify(propostaCreditoService, times(1)).buscarPorId(1L);
    }

    @Test
    void buscarPropostaPorId_DeveRetornarNotFoundQuandoPropostaNaoEncontrada() throws Exception {
        when(propostaCreditoService.buscarPorId(1L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/fictcred/v1/api/proposta-cliente/1"))
                .andExpect(status().isNotFound());

        verify(propostaCreditoService, times(1)).buscarPorId(1L);
    }

    @Test
    void criarProposta_DeveRetornarBadRequestQuandoClienteNaoEncontrado() throws Exception {
        when(propostaCreditoService.criarProposta(eq(1L), any(PropostaCredito.class))).thenThrow(new RuntimeException("Cliente não encontrado"));

        mockMvc.perform(post("/fictcred/v1/api/proposta-cliente/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(propostaCreditoInsertDTO)))
                .andExpect(status().isBadRequest());

        verify(propostaCreditoService, times(1)).criarProposta(eq(1L), any(PropostaCredito.class));
    }

    @Test
    void listarPropostasPorCliente_DeveRetornarNotFoundQuandoClienteNaoEncontrado() throws Exception {
        when(propostaCreditoService.listarPorCliente(1L)).thenThrow(new RuntimeException("Cliente não encontrado"));

        mockMvc.perform(get("/fictcred/v1/api/proposta-cliente/cliente/1"))
                .andExpect(status().isNotFound());

        verify(propostaCreditoService, times(1)).listarPorCliente(1L);
    }

}
