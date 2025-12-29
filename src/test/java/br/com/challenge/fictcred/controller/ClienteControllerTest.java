package br.com.challenge.fictcred.controller;

import br.com.challenge.fictcred.dto.ClienteInsertDTO;
import br.com.challenge.fictcred.dto.ClienteListDTO;
import br.com.challenge.fictcred.dto.ClienteUpdateDTO;
import br.com.challenge.fictcred.model.Cliente;
import br.com.challenge.fictcred.service.ClienteService;
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

class ClienteControllerTest {

    @Mock
    private ClienteService clienteService;

    @InjectMocks
    private ClienteController clienteController;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    private Cliente cliente;
    private ClienteInsertDTO clienteInsertDTO;
    private ClienteUpdateDTO clienteUpdateDTO;
    private ClienteListDTO clienteListDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(clienteController).build();
        objectMapper = new ObjectMapper();

        cliente = new Cliente();
        cliente.setId(1L);
        cliente.setNome("João Silva");
        cliente.setCpf("12345678909");
        cliente.setRendaMensal(new BigDecimal("5000.00"));
        cliente.setDataCadastro(new Date());

        clienteInsertDTO = new ClienteInsertDTO();
        clienteInsertDTO.setNome("João Silva");
        clienteInsertDTO.setCpf("12345678909");
        clienteInsertDTO.setRendaMensal(new BigDecimal("5000.00"));

        clienteUpdateDTO = new ClienteUpdateDTO();
        clienteUpdateDTO.setNome("João Silva Atualizado");
        clienteUpdateDTO.setCpf("12345678909");
        clienteUpdateDTO.setRendaMensal(new BigDecimal("6000.00"));

        clienteListDTO = new ClienteListDTO(1L, "João Silva", "12345678909", new BigDecimal("5000.00"), new Date());
    }

    @Test
    void criarCliente_DeveRetornarClienteCriado() throws Exception {
        when(clienteService.salvar(any(Cliente.class))).thenReturn(cliente);

        mockMvc.perform(post("/fictcred/v1/api/cliente")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(clienteInsertDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.nome").value("João Silva"));

        verify(clienteService, times(1)).salvar(any(Cliente.class));
    }

    @Test
    void atualizarCliente_DeveRetornarClienteAtualizado() throws Exception {
        when(clienteService.atualizar(eq(1L), any(Cliente.class))).thenReturn(cliente);

        mockMvc.perform(put("/fictcred/v1/api/cliente/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(clienteUpdateDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.nome").value("João Silva"));

        verify(clienteService, times(1)).atualizar(eq(1L), any(Cliente.class));
    }

    @Test
    void atualizarCliente_DeveRetornarNotFoundQuandoClienteNaoEncontrado() throws Exception {
        when(clienteService.atualizar(eq(1L), any(Cliente.class))).thenThrow(new RuntimeException("Cliente não encontrado"));

        mockMvc.perform(put("/fictcred/v1/api/cliente/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(clienteUpdateDTO)))
                .andExpect(status().isNotFound());

        verify(clienteService, times(1)).atualizar(eq(1L), any(Cliente.class));
    }

    @Test
    void buscarClientePorId_DeveRetornarCliente() throws Exception {
        when(clienteService.buscarPorId(1L)).thenReturn(Optional.of(cliente));

        mockMvc.perform(get("/fictcred/v1/api/cliente/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.nome").value("João Silva"));

        verify(clienteService, times(1)).buscarPorId(1L);
    }

    @Test
    void buscarClientePorId_DeveRetornarNotFoundQuandoClienteNaoEncontrado() throws Exception {
        when(clienteService.buscarPorId(1L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/fictcred/v1/api/cliente/1"))
                .andExpect(status().isNotFound());

        verify(clienteService, times(1)).buscarPorId(1L);
    }

    @Test
    void listarClientes_DeveRetornarListaDeClientes() throws Exception {
        List<Cliente> clientes = Arrays.asList(cliente);
        when(clienteService.listarTodos()).thenReturn(clientes);

        mockMvc.perform(get("/fictcred/v1/api/cliente"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].nome").value("João Silva"));

        verify(clienteService, times(1)).listarTodos();
    }

    @Test
    void removerCliente_DeveRetornarNoContent() throws Exception {
        doNothing().when(clienteService).deletar(1L);

        mockMvc.perform(delete("/fictcred/v1/api/cliente/1"))
                .andExpect(status().isNoContent());

        verify(clienteService, times(1)).deletar(1L);
    }
}
