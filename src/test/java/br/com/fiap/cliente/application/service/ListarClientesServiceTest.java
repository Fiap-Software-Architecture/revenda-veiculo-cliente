package br.com.fiap.cliente.application.service;

import br.com.fiap.cliente.application.port.output.ClienteRepositoryPort;
import br.com.fiap.cliente.domain.model.Cliente;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ListarClientesServiceTest {

    @Mock
    private ClienteRepositoryPort repository;

    @InjectMocks
    private ListarClientesService service;

    @Test
    void deveExecutarTodosOrdenadosPorPrecoQuandoStatusNaoInformado() {
        // arrange
        Cliente v1 = new Cliente(
                UUID.fromString("11111111-1111-1111-1111-111111111111"),
                "790.481.430-71",
                "João",
                "Silva"
        );

        Cliente v2 = new Cliente(
                UUID.fromString("22222222-2222-2222-2222-222222222222"),
                "493.566.170-40",
                "Maria",
                "Silva"
        );

        List<Cliente> esperado = List.of(v1, v2);
        when(repository.listarOrdenadoPorNome()).thenReturn(esperado);

        // act
        List<Cliente> resultado = service.executar();

        // assert
        assertEquals(esperado, resultado);

        verify(repository, times(1)).listarOrdenadoPorNome();
        verifyNoMoreInteractions(repository);
    }
    
}