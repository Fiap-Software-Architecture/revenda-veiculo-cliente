package br.com.fiap.cliente.infrastructure.adapter.output.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ClienteRepositoryJpa extends JpaRepository<ClienteJpaEntity, UUID> {

    List<ClienteJpaEntity> findAllByOrderByNome();

    boolean existsByCpf(String cpf);

    boolean existsByCpfAndIdNot(String cpf, UUID id);
}

