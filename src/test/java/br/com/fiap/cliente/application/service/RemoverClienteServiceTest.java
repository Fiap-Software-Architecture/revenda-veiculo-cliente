package br.com.fiap.cliente.application.service;

import br.com.fiap.cliente.application.port.output.ClienteRepositoryPort;
import br.com.fiap.cliente.domain.exception.ClienteNaoEncontradoException;
import br.com.fiap.cliente.domain.model.Cliente;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
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
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RemoverClienteServiceTest {

    @Mock
    private ClienteRepositoryPort repository;

    @InjectMocks
    private RemoverClienteService service;

    @Test
    void deveRemoverClienteComSucesso() {
        // arrange
        UUID id = UUID.fromString("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa");
        Cliente cliente = new Cliente(
                id,
                "790.481.430-71",
                "João",
                "Silva"
        );

        when(repository.buscarPorId(id)).thenReturn(Optional.of(cliente));

        // act
        service.executar(id);

        // assert
        verify(repository, times(1)).buscarPorId(id);
        verify(repository, times(1)).removerPorId(id);
        verifyNoMoreInteractions(repository);
    }

    @Test
    void deveLancarExcecaoQuandoClienteNaoEncontrado() {
        // arrange
        UUID id = UUID.fromString("bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb");
        when(repository.buscarPorId(id)).thenReturn(Optional.empty());

        // act + assert
        assertThrows(ClienteNaoEncontradoException.class, () -> service.executar(id));

        verify(repository, times(1)).buscarPorId(id);
        verify(repository, never()).removerPorId(any());
        verifyNoMoreInteractions(repository);
    }
    
}