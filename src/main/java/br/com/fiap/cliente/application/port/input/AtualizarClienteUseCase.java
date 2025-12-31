package br.com.fiap.cliente.application.port.input;

import br.com.fiap.cliente.application.dto.AtualizarClienteCommand;

public interface AtualizarClienteUseCase {

    void executar(AtualizarClienteCommand command);

}
