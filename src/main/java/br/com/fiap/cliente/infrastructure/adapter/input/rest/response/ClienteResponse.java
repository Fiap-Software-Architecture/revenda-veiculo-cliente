package br.com.fiap.cliente.infrastructure.adapter.input.rest.response;

import br.com.fiap.cliente.domain.model.Cliente;

import java.util.UUID;

public record ClienteResponse(
        UUID id,
        String cpf,
        String nome,
        String sobrenome
) {

    public static ClienteResponse fromDomain(Cliente cliente) {
        return new ClienteResponse(
                cliente.getId(),
                cliente.getCpf(),
                cliente.getNome(),
                cliente.getSobrenome()
        );
    }

}

