package br.com.fiap.cliente.application.dto;

public record CadastrarClienteCommand(
        String cpf,
        String nome,
        String sobrenome
) {}
