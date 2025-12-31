package br.com.fiap.cliente.infrastructure.adapter.output.persistence;

import br.com.fiap.cliente.application.port.output.ClienteRepositoryPort;
import br.com.fiap.cliente.domain.model.Cliente;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
@AllArgsConstructor
public class ClientePersistenceAdapter implements ClienteRepositoryPort {

    private final ClienteRepositoryJpa repository;

    @Override
    public Optional<Cliente> buscarPorId(UUID id) {
        return repository.findById(id)
                .map(ClienteJpaEntity::toDomain);
    }

    @Override
    public boolean existePorCpf(String cpf) {
        return repository.existsByCpf(cpf);
    }

    @Override
    public boolean existePorCpfEIdDiferente(String cpf, UUID id) {
        return repository.existsByCpfAndIdNot(cpf, id);
    }

    @Override
    public Cliente salvar(Cliente cliente) {
        ClienteJpaEntity entity = ClienteJpaEntity.fromDomain(cliente);
        ClienteJpaEntity entitySalva = repository.save(entity);
        return entitySalva.toDomain();
    }

    @Override
    public List<Cliente> listarOrdenadoPorNome() {
        return repository.findAllByOrderByNome()
                .stream()
                .map(ClienteJpaEntity::toDomain)
                .toList();
    }

    @Override
    public void removerPorId(UUID id) {
        repository.deleteById(id);
    }

}

