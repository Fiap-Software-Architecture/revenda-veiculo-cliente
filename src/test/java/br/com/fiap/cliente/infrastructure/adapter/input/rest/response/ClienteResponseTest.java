package br.com.fiap.cliente.infrastructure.adapter.input.rest.response;

import br.com.fiap.cliente.domain.model.Cliente;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class ClienteResponseTest {

    @Test
    void deveConverterClienteDomainParaClienteResponse() {
        // arrange
        UUID id = UUID.randomUUID();

        Cliente cliente = new Cliente(
                id,
                "493.566.170-40",
                "João",
                "Silva"
        );

        // act
        ClienteResponse response = ClienteResponse.fromDomain(cliente);

        // assert
        assertNotNull(response);
        assertEquals(id, response.id());
        assertEquals("493.566.170-40", response.cpf());
        assertEquals("João", response.nome());
        assertEquals("Silva", response.sobrenome());
    }
    
}