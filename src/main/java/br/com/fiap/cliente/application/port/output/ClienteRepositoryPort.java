package br.com.fiap.cliente.application.port.output;

import br.com.fiap.cliente.domain.model.Cliente;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ClienteRepositoryPort {

    Optional<Cliente> buscarPorId(UUID id);

    boolean existePorCpf(String cpf);

    boolean existePorCpfEIdDiferente(String cpf, UUID id);

    Cliente salvar(Cliente cliente);

    List<Cliente> listarOrdenadoPorNome();

    void removerPorId(UUID id);

}
