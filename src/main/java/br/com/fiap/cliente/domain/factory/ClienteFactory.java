package br.com.fiap.cliente.domain.factory;

import br.com.fiap.cliente.domain.exception.DomainValidationException;
import br.com.fiap.cliente.domain.model.Cliente;

import java.util.UUID;

public class ClienteFactory {

    public static Cliente novoCadastro(String cpf, String nome, String sobrenome) {
        validarDadosPadroes(cpf, nome, sobrenome);

        return new Cliente(
                UUID.randomUUID(),
                cpf,
                nome,
                sobrenome
        );
    }

    public static Cliente atualizarCadastro(UUID id, String cpf, String nome, String sobrenome) {
        if (id == null) throw new DomainValidationException("id", "id é obrigatório");
        validarDadosPadroes(cpf, nome, sobrenome);

        return new Cliente(
                id,
                cpf,
                nome,
                sobrenome
        );
    }

    private static void validarDadosPadroes(String cpf, String nome, String sobrenome) {
        if (cpf == null || cpf.isBlank()) throw new DomainValidationException("cpf", "cpf é obrigatório");
        if (nome == null || nome.isBlank()) throw new DomainValidationException("nome", "nome é obrigatório");
        if (sobrenome == null || sobrenome.isBlank()) throw new DomainValidationException("sobrenome", "sobrenome é obrigatório");
    }

}
