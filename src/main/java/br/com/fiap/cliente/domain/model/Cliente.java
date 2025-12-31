package br.com.fiap.cliente.domain.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@Data
@AllArgsConstructor
public class Cliente {

    private final UUID id;
    private final String cpf;
    private final String nome;
    private final String sobrenome;
    
}

