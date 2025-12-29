package br.com.challenge.fictcred.service;

import br.com.challenge.fictcred.model.Cliente;
import br.com.challenge.fictcred.repository.ClienteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ClienteServiceTest {

    @Mock
    private ClienteRepository clienteRepository;

    @InjectMocks
    private ClienteService clienteService;

    private Cliente cliente;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        cliente = new Cliente();
        cliente.setId(1L);
        cliente.setNome("João Silva");
        cliente.setCpf("12345678900");
        cliente.setRendaMensal(new BigDecimal("5000.00"));
        cliente.setDataCadastro(new Date());
    }

    @Test
    void salvar_DeveSalvarCliente() {
        when(clienteRepository.save(any(Cliente.class))).thenReturn(cliente);

        Cliente resultado = clienteService.salvar(cliente);

        assertNotNull(resultado);
        assertEquals("João Silva", resultado.getNome());
        assertEquals("12345678900", resultado.getCpf());
        verify(clienteRepository, times(1)).save(any(Cliente.class));
    }

    @Test
    void buscarPorId_DeveRetornarClienteQuandoEncontrado() {
        when(clienteRepository.findById(1L)).thenReturn(Optional.of(cliente));

        Optional<Cliente> resultado = clienteService.buscarPorId(1L);

        assertTrue(resultado.isPresent());
        assertEquals(1L, resultado.get().getId());
        verify(clienteRepository, times(1)).findById(1L);
    }

    @Test
    void buscarPorId_DeveRetornarVazioQuandoNaoEncontrado() {
        when(clienteRepository.findById(1L)).thenReturn(Optional.empty());

        Optional<Cliente> resultado = clienteService.buscarPorId(1L);

        assertFalse(resultado.isPresent());
        verify(clienteRepository, times(1)).findById(1L);
    }

    @Test
    void listarTodos_DeveRetornarListaDeClientes() {
        List<Cliente> clientes = Arrays.asList(cliente);
        when(clienteRepository.findAll()).thenReturn(clientes);

        List<Cliente> resultado = clienteService.listarTodos();

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals(1L, resultado.get(0).getId());
        verify(clienteRepository, times(1)).findAll();
    }

    @Test
    void atualizar_DeveAtualizarClienteQuandoEncontrado() {
        Cliente clienteAtualizado = new Cliente();
        clienteAtualizado.setNome("João Silva Atualizado");
        clienteAtualizado.setCpf("12345678900");
        clienteAtualizado.setRendaMensal(new BigDecimal("6000.00"));
        clienteAtualizado.setDataCadastro(new Date());

        when(clienteRepository.findById(1L)).thenReturn(Optional.of(cliente));
        when(clienteRepository.save(any(Cliente.class))).thenReturn(clienteAtualizado);

        Cliente resultado = clienteService.atualizar(1L, clienteAtualizado);

        assertNotNull(resultado);
        assertEquals("João Silva Atualizado", resultado.getNome());
        verify(clienteRepository, times(1)).findById(1L);
        verify(clienteRepository, times(1)).save(any(Cliente.class));
    }

    @Test
    void atualizar_DeveLancarExcecaoQuandoClienteNaoEncontrado() {
        when(clienteRepository.findById(1L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            clienteService.atualizar(1L, cliente);
        });

        assertEquals("Cliente não encontrado", exception.getMessage());
        verify(clienteRepository, times(1)).findById(1L);
        verify(clienteRepository, never()).save(any(Cliente.class));
    }

    @Test
    void deletar_DeveDeletarClienteQuandoEncontrado() {
        when(clienteRepository.findById(1L)).thenReturn(Optional.of(cliente));

        assertDoesNotThrow(() -> clienteService.deletar(1L));

        verify(clienteRepository, times(1)).findById(1L);
        verify(clienteRepository, times(1)).delete(cliente);
    }

    @Test
    void deletar_DeveLancarExcecaoQuandoClienteNaoEncontrado() {
        when(clienteRepository.findById(1L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            clienteService.deletar(1L);
        });

        assertEquals("Cliente não encontrado", exception.getMessage());
        verify(clienteRepository, times(1)).findById(1L);
        verify(clienteRepository, never()).delete(any(Cliente.class));
    }
}
