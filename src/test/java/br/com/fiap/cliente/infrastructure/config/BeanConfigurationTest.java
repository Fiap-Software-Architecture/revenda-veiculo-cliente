package br.com.fiap.cliente.infrastructure.config;

import br.com.fiap.cliente.application.port.input.AtualizarClienteUseCase;
import br.com.fiap.cliente.application.port.input.BuscarClientePorIdUseCase;
import br.com.fiap.cliente.application.port.input.CadastrarClienteUseCase;
import br.com.fiap.cliente.application.port.input.ListarClientesUseCase;
import br.com.fiap.cliente.application.port.input.RemoverClienteUseCase;
import br.com.fiap.cliente.application.port.output.ClienteRepositoryPort;
import br.com.fiap.cliente.domain.factory.ClienteFactory;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

class BeanConfigurationTest {

    private final ApplicationContextRunner contextRunner = new ApplicationContextRunner()
            .withUserConfiguration(BeanConfiguration.class)
            .withBean(ClienteRepositoryPort.class, () -> mock(ClienteRepositoryPort.class))
            .withBean(ClienteFactory.class, () -> mock(ClienteFactory.class));

    @Test
    void deveCriarBeanCadastrarClienteUseCase() {
        contextRunner.run(context -> {
            assertThat(context).hasSingleBean(CadastrarClienteUseCase.class);
            assertThat(context.getBean(CadastrarClienteUseCase.class)).isNotNull();
        });
    }

    @Test
    void deveCriarBeanAtualizarClienteUseCase() {
        contextRunner.run(context -> {
            assertThat(context).hasSingleBean(AtualizarClienteUseCase.class);
            assertThat(context.getBean(AtualizarClienteUseCase.class)).isNotNull();
        });
    }

    @Test
    void deveCriarBeanListarClientesUseCase() {
        contextRunner.run(context -> {
            assertThat(context).hasSingleBean(ListarClientesUseCase.class);
            assertThat(context.getBean(ListarClientesUseCase.class)).isNotNull();
        });
    }

    @Test
    void deveCriarBeanBuscarClientePorIdUseCase() {
        contextRunner.run(context -> {
            assertThat(context).hasSingleBean(BuscarClientePorIdUseCase.class);
            assertThat(context.getBean(BuscarClientePorIdUseCase.class)).isNotNull();
        });
    }

    @Test
    void deveCriarBeanRemoverClienteUseCase() {
        contextRunner.run(context -> {
            assertThat(context).hasSingleBean(RemoverClienteUseCase.class);
            assertThat(context.getBean(RemoverClienteUseCase.class)).isNotNull();
        });
    }

}