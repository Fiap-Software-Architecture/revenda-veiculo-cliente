package br.com.fiap.cliente.application.service;

import br.com.fiap.cliente.application.dto.AtualizarClienteCommand;
import br.com.fiap.cliente.application.port.input.AtualizarClienteUseCase;
import br.com.fiap.cliente.application.port.output.ClienteRepositoryPort;
import br.com.fiap.cliente.domain.exception.ClienteNaoEncontradoException;
import br.com.fiap.cliente.domain.exception.CpfJaCadastradoException;
import br.com.fiap.cliente.domain.factory.ClienteFactory;
import br.com.fiap.cliente.domain.model.Cliente;
import lombok.AllArgsConstructor;

import java.util.UUID;

@AllArgsConstructor
public class AtualizarClienteService implements AtualizarClienteUseCase {

    private final ClienteRepositoryPort repository;

    @Override
    public void executar(AtualizarClienteCommand command) {
        UUID id = command.id();

        Cliente atual = repository.buscarPorId(id)
                .orElseThrow(() -> new ClienteNaoEncontradoException(id));

        if (repository.existePorCpfEIdDiferente(command.cpf(), id)) {
            throw new CpfJaCadastradoException(command.cpf());
        }

        Cliente atualizado = ClienteFactory.atualizarCadastro(
                atual.getId(),
                command.cpf(),
                command.nome(),
                command.sobrenome()
        );

        repository.salvar(atualizado);
    }
}
