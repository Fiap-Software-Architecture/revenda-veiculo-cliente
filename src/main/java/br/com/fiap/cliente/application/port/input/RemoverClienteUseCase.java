package br.com.fiap.cliente.application.port.input;

import java.util.UUID;

public interface RemoverClienteUseCase {

    void executar(UUID id);

}
