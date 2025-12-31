package br.com.fiap.cliente.application.service;

import br.com.fiap.cliente.application.port.input.ListarClientesUseCase;
import br.com.fiap.cliente.application.port.output.ClienteRepositoryPort;
import br.com.fiap.cliente.domain.model.Cliente;
import lombok.AllArgsConstructor;

import java.util.List;

@AllArgsConstructor
public class ListarClientesService implements ListarClientesUseCase {

    private final ClienteRepositoryPort repository;

    @Override
    public List<Cliente> executar() {
        return repository.listarOrdenadoPorNome();
    }
}

