package br.com.challenge.fictcred.service;

import br.com.challenge.fictcred.enums.StatusParcela;
import br.com.challenge.fictcred.model.Cliente;
import br.com.challenge.fictcred.model.PropostaCredito;
import br.com.challenge.fictcred.repository.ClienteRepository;
import br.com.challenge.fictcred.repository.PropostaCreditoRepository;
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

class PropostaCreditoServiceTest {

    @Mock
    private PropostaCreditoRepository propostaCreditoRepository;

    @Mock
    private ClienteRepository clienteRepository;

    @InjectMocks
    private PropostaCreditoService propostaCreditoService;

    private Cliente cliente;
    private PropostaCredito proposta;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        cliente = new Cliente();
        cliente.setId(1L);
        cliente.setNome("João Silva");
        cliente.setCpf("12345678900");
        cliente.setRendaMensal(new BigDecimal("5000.00"));
        cliente.setDataCadastro(new Date());

        proposta = new PropostaCredito();
        proposta.setId(1L);
        proposta.setValorSolicitado(new BigDecimal("20000.00"));
        proposta.setNumeroParcelas(12);
        proposta.setCliente(cliente);
        proposta.setDataCriacao(new Date());
    }

    @Test
    void criarProposta_DeveCriarPropostaAprovadaQuandoAtendeRegras() {
        when(clienteRepository.findById(1L)).thenReturn(Optional.of(cliente));
        when(propostaCreditoRepository.save(any(PropostaCredito.class))).thenReturn(proposta);

        PropostaCredito resultado = propostaCreditoService.criarProposta(1L, proposta);

        assertNotNull(resultado);
        assertEquals(StatusParcela.APROVADA, resultado.getStatus());
        verify(clienteRepository, times(1)).findById(1L);
        verify(propostaCreditoRepository, times(1)).save(any(PropostaCredito.class));
    }

    @Test
    void criarProposta_DeveCriarPropostaReprovadaQuandoValorExcedeLimite() {
        proposta.setValorSolicitado(new BigDecimal("30000.00")); // 6x renda mensal

        when(clienteRepository.findById(1L)).thenReturn(Optional.of(cliente));
        when(propostaCreditoRepository.save(any(PropostaCredito.class))).thenReturn(proposta);

        PropostaCredito resultado = propostaCreditoService.criarProposta(1L, proposta);

        assertNotNull(resultado);
        assertEquals(StatusParcela.REPROVADA, resultado.getStatus());
        verify(clienteRepository, times(1)).findById(1L);
        verify(propostaCreditoRepository, times(1)).save(any(PropostaCredito.class));
    }

    @Test
    void criarProposta_DeveCriarPropostaReprovadaQuandoParcelasForaDoLimite() {
        proposta.setNumeroParcelas(30); // Mais de 24 parcelas

        when(clienteRepository.findById(1L)).thenReturn(Optional.of(cliente));
        when(propostaCreditoRepository.save(any(PropostaCredito.class))).thenReturn(proposta);

        PropostaCredito resultado = propostaCreditoService.criarProposta(1L, proposta);

        assertNotNull(resultado);
        assertEquals(StatusParcela.REPROVADA, resultado.getStatus());
        verify(clienteRepository, times(1)).findById(1L);
        verify(propostaCreditoRepository, times(1)).save(any(PropostaCredito.class));
    }

    @Test
    void criarProposta_DeveLancarExcecaoQuandoClienteNaoEncontrado() {
        when(clienteRepository.findById(1L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            propostaCreditoService.criarProposta(1L, proposta);
        });

        assertEquals("Cliente não encontrado", exception.getMessage());
        verify(clienteRepository, times(1)).findById(1L);
        verify(propostaCreditoRepository, never()).save(any(PropostaCredito.class));
    }

    @Test
    void buscarPorId_DeveRetornarPropostaQuandoEncontrada() {
        when(propostaCreditoRepository.findById(1L)).thenReturn(Optional.of(proposta));

        Optional<PropostaCredito> resultado = propostaCreditoService.buscarPorId(1L);

        assertTrue(resultado.isPresent());
        assertEquals(1L, resultado.get().getId());
        verify(propostaCreditoRepository, times(1)).findById(1L);
    }

    @Test
    void buscarPorId_DeveRetornarVazioQuandoNaoEncontrada() {
        when(propostaCreditoRepository.findById(1L)).thenReturn(Optional.empty());

        Optional<PropostaCredito> resultado = propostaCreditoService.buscarPorId(1L);

        assertFalse(resultado.isPresent());
        verify(propostaCreditoRepository, times(1)).findById(1L);
    }

    @Test
    void listarPorCliente_DeveRetornarListaDePropostas() {
        List<PropostaCredito> propostas = Arrays.asList(proposta);
        when(clienteRepository.findById(1L)).thenReturn(Optional.of(cliente));
        when(propostaCreditoRepository.findByCliente(cliente)).thenReturn(propostas);

        List<PropostaCredito> resultado = propostaCreditoService.listarPorCliente(1L);

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals(1L, resultado.get(0).getId());
        verify(clienteRepository, times(1)).findById(1L);
        verify(propostaCreditoRepository, times(1)).findByCliente(cliente);
    }

    @Test
    void listarPorCliente_DeveLancarExcecaoQuandoClienteNaoEncontrado() {
        when(clienteRepository.findById(1L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            propostaCreditoService.listarPorCliente(1L);
        });

        assertEquals("Cliente não encontrado", exception.getMessage());
        verify(clienteRepository, times(1)).findById(1L);
        verify(propostaCreditoRepository, never()).findByCliente(any(Cliente.class));
    }
}
