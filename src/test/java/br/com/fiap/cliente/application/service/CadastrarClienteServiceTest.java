package br.com.fiap.cliente.application.service;

import br.com.fiap.cliente.application.dto.CadastrarClienteCommand;
import br.com.fiap.cliente.application.port.output.ClienteRepositoryPort;
import br.com.fiap.cliente.domain.exception.CpfJaCadastradoException;
import br.com.fiap.cliente.domain.model.Cliente;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CadastrarClienteServiceTest {

    @Mock
    private ClienteRepositoryPort repository;

    @InjectMocks
    private CadastrarClienteService service;

    private CadastrarClienteCommand command;

    @BeforeEach
    void setUp() {
        command = new CadastrarClienteCommand(
                "790.481.430-71",
                "João",
                "Silva"
        );
    }

    @Test
    void deveCadastrarClienteComSucessoERetornarId() {
        // arrange
        when(repository.existePorCpf(command.cpf()))
                .thenReturn(false);

        when(repository.salvar(any(Cliente.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        // act
        UUID idRetornado = service.executar(command);

        // assert
        assertNotNull(idRetornado);

        verify(repository, times(1)).existePorCpf(command.cpf());
        verify(repository, times(1)).salvar(any(Cliente.class));
    }

    @Test
    void deveAtivarClienteAntesDeSalvar() {
        // arrange
        when(repository.existePorCpf(command.cpf()))
                .thenReturn(false);

        ArgumentCaptor<Cliente> clienteCaptor =
                ArgumentCaptor.forClass(Cliente.class);

        when(repository.salvar(clienteCaptor.capture()))
                .thenAnswer(invocation -> invocation.getArgument(0));

        // act
        service.executar(command);

        // assert
        Cliente clienteSalvo = clienteCaptor.getValue();

        assertNotNull(clienteSalvo.getId());
        assertEquals(command.cpf(), clienteSalvo.getCpf());
        assertEquals(command.nome(), clienteSalvo.getNome());
        assertEquals(command.sobrenome(), clienteSalvo.getSobrenome());
    }

    @Test
    void deveLancarExcecaoQuandoCpfJaExiste() {
        // arrange
        when(repository.existePorCpf(command.cpf()))
                .thenReturn(true);

        // act + assert
        assertThrows(
                CpfJaCadastradoException.class,
                () -> service.executar(command)
        );

        verify(repository, times(1)).existePorCpf(command.cpf());
        verify(repository, never()).salvar(any());
    }
    
}