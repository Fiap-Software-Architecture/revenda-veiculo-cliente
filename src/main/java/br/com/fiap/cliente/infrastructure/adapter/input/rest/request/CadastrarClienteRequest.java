package br.com.fiap.cliente.infrastructure.adapter.input.rest.request;

import br.com.fiap.cliente.application.dto.CadastrarClienteCommand;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.hibernate.validator.constraints.br.CPF;

@Data
public class CadastrarClienteRequest {

    @NotBlank
    @CPF
    private String cpf;

    @NotBlank
    private String nome;

    @NotBlank
    private String sobrenome;

    public CadastrarClienteCommand toCommand() {
        return new CadastrarClienteCommand(
                cpf,
                nome,
                sobrenome
        );
    }

}

