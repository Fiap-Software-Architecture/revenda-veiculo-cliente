package br.com.fiap.cliente.application.service;

import br.com.fiap.cliente.application.port.input.BuscarClientePorIdUseCase;
import br.com.fiap.cliente.application.port.output.ClienteRepositoryPort;
import br.com.fiap.cliente.domain.exception.ClienteNaoEncontradoException;
import br.com.fiap.cliente.domain.model.Cliente;
import lombok.AllArgsConstructor;

import java.util.UUID;

@AllArgsConstructor
public class BuscarClientePorIdService implements BuscarClientePorIdUseCase {

    private final ClienteRepositoryPort repository;

    @Override
    public Cliente executar(UUID id) {
        return repository.buscarPorId(id)
                .orElseThrow(() -> new ClienteNaoEncontradoException(id));
    }
}
