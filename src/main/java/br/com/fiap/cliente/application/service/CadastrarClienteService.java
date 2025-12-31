package br.com.fiap.cliente.application.service;

import br.com.fiap.cliente.application.dto.CadastrarClienteCommand;
import br.com.fiap.cliente.application.port.input.CadastrarClienteUseCase;
import br.com.fiap.cliente.application.port.output.ClienteRepositoryPort;
import br.com.fiap.cliente.domain.exception.CpfJaCadastradoException;
import br.com.fiap.cliente.domain.factory.ClienteFactory;
import br.com.fiap.cliente.domain.model.Cliente;
import lombok.AllArgsConstructor;

import java.util.UUID;

@AllArgsConstructor
public class CadastrarClienteService implements CadastrarClienteUseCase {

    private final ClienteRepositoryPort repository;

    @Override
    public UUID executar(CadastrarClienteCommand command) {

        if (repository.existePorCpf(command.cpf())) {
            throw new CpfJaCadastradoException(command.cpf());
        }

        Cliente cliente = ClienteFactory.novoCadastro(
                command.cpf(),
                command.nome(),
                command.sobrenome()
        );

        Cliente clienteSalvo = repository.salvar(cliente);

        return clienteSalvo.getId();
    }
}

