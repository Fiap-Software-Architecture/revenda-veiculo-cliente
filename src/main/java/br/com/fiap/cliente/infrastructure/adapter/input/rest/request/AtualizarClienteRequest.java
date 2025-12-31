package br.com.fiap.cliente.infrastructure.adapter.input.rest.request;

import br.com.fiap.cliente.application.dto.AtualizarClienteCommand;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.hibernate.validator.constraints.br.CPF;

import java.util.UUID;

@Data
public class AtualizarClienteRequest {

    @NotBlank
    @CPF
    private String cpf;

    @NotBlank
    private String nome;

    @NotBlank
    private String sobrenome;

    public AtualizarClienteCommand toCommand(UUID id) {
        return new AtualizarClienteCommand(
                id,
                cpf,
                nome,
                sobrenome
        );
    }

}
