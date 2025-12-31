package br.com.fiap.cliente.infrastructure.config;

import br.com.fiap.cliente.application.port.input.AtualizarClienteUseCase;
import br.com.fiap.cliente.application.port.input.BuscarClientePorIdUseCase;
import br.com.fiap.cliente.application.port.input.CadastrarClienteUseCase;
import br.com.fiap.cliente.application.port.input.ListarClientesUseCase;
import br.com.fiap.cliente.application.port.input.RemoverClienteUseCase;
import br.com.fiap.cliente.application.port.output.ClienteRepositoryPort;
import br.com.fiap.cliente.application.service.AtualizarClienteService;
import br.com.fiap.cliente.application.service.BuscarClientePorIdService;
import br.com.fiap.cliente.application.service.CadastrarClienteService;
import br.com.fiap.cliente.application.service.ListarClientesService;
import br.com.fiap.cliente.application.service.RemoverClienteService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanConfiguration {

    @Bean
    CadastrarClienteUseCase cadastrarClienteUseCase(
            ClienteRepositoryPort repository
    ) {
        return new CadastrarClienteService(repository);
    }

    @Bean
    AtualizarClienteUseCase atualizarClienteUseCase(
            ClienteRepositoryPort repository
    ) {
        return new AtualizarClienteService(repository);
    }

    @Bean
    ListarClientesUseCase listarClientesUseCase(
            ClienteRepositoryPort repository
    ) {
        return new ListarClientesService(repository);
    }

    @Bean
    public BuscarClientePorIdUseCase buscarClientePorIdUseCase(
            ClienteRepositoryPort repositoryPort
    ) {
        return new BuscarClientePorIdService(repositoryPort);
    }

    @Bean
    public RemoverClienteUseCase removerClienteUseCase(
            ClienteRepositoryPort repositoryPort
    ) {
        return new RemoverClienteService(repositoryPort);
    }

}
