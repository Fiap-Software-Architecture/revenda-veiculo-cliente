package br.com.fiap.cliente.application.port.input;

import br.com.fiap.cliente.domain.model.Cliente;

import java.util.UUID;

public interface BuscarClientePorIdUseCase {

    Cliente executar(UUID id);

}
