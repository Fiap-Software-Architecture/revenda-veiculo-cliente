package br.com.fiap.cliente.application.service;

import br.com.fiap.cliente.application.dto.AtualizarClienteCommand;
import br.com.fiap.cliente.application.port.output.ClienteRepositoryPort;
import br.com.fiap.cliente.domain.exception.ClienteNaoEncontradoException;
import br.com.fiap.cliente.domain.exception.CpfJaCadastradoException;
import br.com.fiap.cliente.domain.model.Cliente;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AtualizarClienteServiceTest {

    @Mock
    private ClienteRepositoryPort repository;

    @InjectMocks
    private AtualizarClienteService service;

    private UUID id;
    private AtualizarClienteCommand command;

    @BeforeEach
    void setUp() {
        id = UUID.fromString("22222222-2222-2222-2222-222222222222");

        command = new AtualizarClienteCommand(
                id,
                "790.481.430-71",
                "João",
                "Silva"
        );
    }

    @Test
    void deveAtualizarClienteComSucesso() {
        // arrange
        Cliente clienteAtual = new Cliente(
                id,
                "790.481.430-71",
                "João",
                "Silva"
        );

        when(repository.buscarPorId(id)).thenReturn(Optional.of(clienteAtual));
        when(repository.existePorCpfEIdDiferente(command.cpf(), id)).thenReturn(false);

        ArgumentCaptor<Cliente> clienteCaptor = ArgumentCaptor.forClass(Cliente.class);
        when(repository.salvar(clienteCaptor.capture()))
                .thenAnswer(invocation -> invocation.getArgument(0));

        // act
        service.executar(command);

        // assert
        verify(repository, times(1)).buscarPorId(id);
        verify(repository, times(1)).existePorCpfEIdDiferente(command.cpf(), id);
        verify(repository, times(1)).salvar(any(Cliente.class));

        Cliente clienteSalvo = clienteCaptor.getValue();
        assertNotNull(clienteSalvo);

        assertEquals(id, clienteSalvo.getId());
        assertEquals(command.cpf(), clienteSalvo.getCpf());
        assertEquals(command.nome(), clienteSalvo.getNome());
        assertEquals(command.sobrenome(), clienteSalvo.getSobrenome());
    }

    @Test
    void deveLancarExcecaoQuandoClienteNaoExiste() {
        // arrange
        when(repository.buscarPorId(id)).thenReturn(Optional.empty());

        // act + assert
        assertThrows(
                ClienteNaoEncontradoException.class,
                () -> service.executar(command)
        );

        verify(repository, times(1)).buscarPorId(id);
        verify(repository, never()).existePorCpfEIdDiferente(any(), any());
        verify(repository, never()).salvar(any());
    }

    @Test
    void deveLancarExcecaoQuandoCpfJaExisteParaOutroId() {
        // arrange
        Cliente clienteAtual = new Cliente(
                id,
                "790.481.430-71",
                "João",
                "Silva"
        );

        when(repository.buscarPorId(id)).thenReturn(Optional.of(clienteAtual));
        when(repository.existePorCpfEIdDiferente(command.cpf(), id)).thenReturn(true);

        // act + assert
        assertThrows(
                CpfJaCadastradoException.class,
                () -> service.executar(command)
        );

        verify(repository, times(1)).buscarPorId(id);
        verify(repository, times(1)).existePorCpfEIdDiferente(command.cpf(), id);
        verify(repository, never()).salvar(any());
    }
  
}