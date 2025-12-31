package br.com.fiap.cliente.domain.exception;

public class CpfJaCadastradoException extends RuntimeException {
    public CpfJaCadastradoException(String cpf) {
        super("Cpf já cadastrado: " + cpf);
    }
}
