package br.com.fiap.cliente.infrastructure.adapter.output.persistence;

import br.com.fiap.cliente.domain.model.Cliente;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.util.UUID;

@Entity
@Table(name = "clientes")
public class ClienteJpaEntity {

    @Id
    private UUID id;

    @Column(name = "cpf", nullable = false, unique = true)
    private String cpf;

    private String nome;

    private String sobrenome;


    public static ClienteJpaEntity fromDomain(Cliente cliente) {
        ClienteJpaEntity entity = new ClienteJpaEntity();
        entity.id = cliente.getId();
        entity.cpf = cliente.getCpf();
        entity.nome = cliente.getNome();
        entity.sobrenome = cliente.getSobrenome();
        return entity;
    }

    public Cliente toDomain() {
        return new Cliente(
                this.id,
                this.cpf,
                this.nome,
                this.sobrenome
        );
    }
}

