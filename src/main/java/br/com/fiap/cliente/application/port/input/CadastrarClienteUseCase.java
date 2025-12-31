package br.com.fiap.cliente.application.port.input;

import br.com.fiap.cliente.application.dto.CadastrarClienteCommand;

import java.util.UUID;

public interface CadastrarClienteUseCase {

    UUID executar(CadastrarClienteCommand command);

}
