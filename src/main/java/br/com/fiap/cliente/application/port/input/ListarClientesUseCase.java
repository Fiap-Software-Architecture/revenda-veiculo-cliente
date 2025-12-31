package br.com.fiap.cliente.application.port.input;

import br.com.fiap.cliente.domain.model.Cliente;

import java.util.List;

public interface ListarClientesUseCase {

    List<Cliente> executar();

}

