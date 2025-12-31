package br.com.fiap.cliente.application.service;

import br.com.fiap.cliente.application.port.input.RemoverClienteUseCase;
import br.com.fiap.cliente.application.port.output.ClienteRepositoryPort;
import br.com.fiap.cliente.domain.exception.ClienteNaoEncontradoException;
import lombok.AllArgsConstructor;

import java.util.UUID;

@AllArgsConstructor
public class RemoverClienteService implements RemoverClienteUseCase {

    private final ClienteRepositoryPort repository;

    @Override
    public void executar(UUID id) {
        repository.buscarPorId(id)
                .orElseThrow(() -> new ClienteNaoEncontradoException(id));
        repository.removerPorId(id);
    }
}
