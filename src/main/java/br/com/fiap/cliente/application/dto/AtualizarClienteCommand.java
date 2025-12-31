package br.com.fiap.cliente.application.dto;

import java.util.UUID;

public record AtualizarClienteCommand(
        UUID id,
        String cpf,
        String nome,
        String sobrenome
) {}
