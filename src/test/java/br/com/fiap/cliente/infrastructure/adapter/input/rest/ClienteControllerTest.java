package br.com.fiap.cliente.infrastructure.adapter.input.rest;

import br.com.fiap.cliente.application.dto.AtualizarClienteCommand;
import br.com.fiap.cliente.application.dto.CadastrarClienteCommand;
import br.com.fiap.cliente.application.port.input.AtualizarClienteUseCase;
import br.com.fiap.cliente.application.port.input.BuscarClientePorIdUseCase;
import br.com.fiap.cliente.application.port.input.CadastrarClienteUseCase;
import br.com.fiap.cliente.application.port.input.ListarClientesUseCase;
import br.com.fiap.cliente.application.port.input.RemoverClienteUseCase;
import br.com.fiap.cliente.domain.exception.ClienteNaoEncontradoException;
import br.com.fiap.cliente.domain.model.Cliente;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.security.oauth2.server.resource.autoconfigure.servlet.OAuth2ResourceServerAutoConfiguration;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WithMockUser
@WebMvcTest(controllers = ClienteController.class,
        excludeAutoConfiguration = {
               OAuth2ResourceServerAutoConfiguration.class
        }
)
class ClienteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private BuscarClientePorIdUseCase buscarClientePorIdUseCase;
    @MockitoBean
    private ListarClientesUseCase listarClientesUseCase;
    @MockitoBean
    private CadastrarClienteUseCase cadastrarClienteUseCase;
    @MockitoBean
    private AtualizarClienteUseCase alterarClienteUseCase;
    @MockitoBean
    private RemoverClienteUseCase removerClienteUseCase;

    @Test
    void cadastrar_deveRetornar201ComJsonId() throws Exception {
        // arrange
        UUID idGerado = UUID.fromString("11111111-1111-1111-1111-111111111111");
        when(cadastrarClienteUseCase.executar(any()))
                .thenReturn(idGerado);

        String payload = """
                {
                  "cpf": "493.566.170-40",
                  "nome": "João",
                  "sobrenome": "Silva"
                }
                """;

        // act + assert
        mockMvc.perform(post("/clientes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isCreated())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(idGerado.toString()));

        verify(cadastrarClienteUseCase, times(1)).executar(any());
    }

    @Test
    void cadastrar_quandoRequestInvalido_deveRetornar400() throws Exception {
        // arrange
        String payloadInvalido = """
                {
                  "cpf": "",
                  "nome": "João",
                  "sobrenome": "Silva"
                }
                """;

        // act + assert
        mockMvc.perform(post("/clientes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payloadInvalido))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(cadastrarClienteUseCase);
    }

    @Test
    void cadastrar_deveChamarUseCaseComCommand() throws Exception {
        // arrange
        UUID idGerado = UUID.fromString("11111111-1111-1111-1111-111111111111");
        when(cadastrarClienteUseCase.executar(any()))
                .thenReturn(idGerado);

        String payload = """
                {
                  "cpf": "493.566.170-40",
                  "nome": "João",
                  "sobrenome": "Silva"
                }
                """;

        ArgumentCaptor<Object> commandCaptor = ArgumentCaptor.forClass(Object.class);

        // act
        mockMvc.perform(post("/clientes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isCreated());

        // assert (mínimo): garante que o controller gerou um command e passou ao use case
        verify(cadastrarClienteUseCase).executar((CadastrarClienteCommand) commandCaptor.capture());
        assertNotNull(commandCaptor.getValue());
    }

    @Test
    void atualizar_deveRetornar204EChamarUseCase() throws Exception {
        // arrange
        UUID id = UUID.fromString("22222222-2222-2222-2222-222222222222");

        String payload = """
                {
                  "cpf": "493.566.170-40",
                  "nome": "João",
                  "sobrenome": "Silva"
                }
                """;

        ArgumentCaptor<AtualizarClienteCommand> captor = ArgumentCaptor.forClass(AtualizarClienteCommand.class);

        // act + assert
        mockMvc.perform(put("/clientes/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isNoContent())
                .andExpect(content().string(""));

        verify(alterarClienteUseCase, times(1)).executar(captor.capture());
        AtualizarClienteCommand command = captor.getValue();

        assertEquals(id, command.id());
        assertEquals("493.566.170-40", command.cpf());
        assertEquals("João", command.nome());
        assertEquals("Silva", command.sobrenome());
    }

    @Test
    void atualizar_quandoRequestInvalido_deveRetornar400ENaoChamarUseCase() throws Exception {
        // arrange
        UUID id = UUID.fromString("33333333-3333-3333-3333-333333333333");

        String payloadInvalido = """
                {
                  "cpf": "493.566.170-40",
                  "nome": "",
                  "sobrenome": "Silva"
                }
                """;

        // act + assert
        mockMvc.perform(put("/clientes/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payloadInvalido))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(alterarClienteUseCase);
    }

    @Test
    void buscarPorId_deveRetornar200ComCliente() throws Exception {
        // arrange
        UUID id = UUID.fromString("99999999-9999-9999-9999-999999999999");

        Cliente cliente = new Cliente(
                id,
                "493.566.170-40",
                "João",
                "Silva"
        );

        when(buscarClientePorIdUseCase.executar(id))
                .thenReturn(cliente);

        // act + assert
        mockMvc.perform(get("/clientes/{id}", id))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(id.toString()))
                .andExpect(jsonPath("$.cpf").value("493.566.170-40"))
                .andExpect(jsonPath("$.nome").value("João"))
                .andExpect(jsonPath("$.sobrenome").value("Silva"));

        verify(buscarClientePorIdUseCase, times(1)).executar(id);
        verifyNoMoreInteractions(buscarClientePorIdUseCase);
    }

    @Test
    void buscarPorId_quandoNaoEncontrado_deveRetornar404() throws Exception {
        // arrange
        UUID id = UUID.fromString("88888888-8888-8888-8888-888888888888");

        when(buscarClientePorIdUseCase.executar(id))
                .thenThrow(new ClienteNaoEncontradoException(id));

        // act + assert
        mockMvc.perform(get("/clientes/{id}", id))
                .andExpect(status().isNotFound())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.error").value("Not Found"))
                .andExpect(jsonPath("$.message").value("Cliente não encontrado. id=" + id))
                .andExpect(jsonPath("$.path").value("/clientes/" + id));

        verify(buscarClientePorIdUseCase, times(1)).executar(id);
        verifyNoMoreInteractions(buscarClientePorIdUseCase);
    }

    @Test
    void listar_deveRetornar200ComListaDeClientes() throws Exception {
        // arrange
        Cliente v1 = new Cliente(
                UUID.fromString("11111111-1111-1111-1111-111111111111"),
                "493.566.170-40",
                "João",
                "Silva"
        );

        Cliente v2 = new Cliente(
                UUID.fromString("22222222-2222-2222-2222-222222222222"),
                "245.701.440-98",
                "Maria",
                "Silva"
        );

        when(listarClientesUseCase.executar())
                .thenReturn(List.of(v1, v2));

        // act + assert
        mockMvc.perform(get("/clientes"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").value(v1.getId().toString()))
                .andExpect(jsonPath("$[0].cpf").value(v1.getCpf()))
                .andExpect(jsonPath("$[0].nome").value(v1.getNome()))
                .andExpect(jsonPath("$[0].sobrenome").value(v1.getSobrenome()))

                .andExpect(jsonPath("$[1].id").value(v2.getId().toString()))
                .andExpect(jsonPath("$[1].cpf").value(v2.getCpf()))
                .andExpect(jsonPath("$[1].nome").value(v2.getNome()))
                .andExpect(jsonPath("$[1].sobrenome").value(v2.getSobrenome()));

        verify(listarClientesUseCase, times(1)).executar();
        verifyNoMoreInteractions(listarClientesUseCase);
    }

    @Test
    void remover_deveRetornar204EChamarUseCase() throws Exception {
        // arrange
        UUID id = UUID.fromString("44444444-4444-4444-4444-444444444444");

        // act + assert
        mockMvc.perform(delete("/clientes/{id}", id))
                .andExpect(status().isNoContent())
                .andExpect(content().string(""));

        verify(removerClienteUseCase, times(1)).executar(id);
        verifyNoMoreInteractions(removerClienteUseCase);
    }

    @Test
    void remover_quandoNaoEncontrado_deveRetornar404() throws Exception {
        // arrange
        UUID id = UUID.fromString("55555555-5555-5555-5555-555555555555");

        doThrow(new ClienteNaoEncontradoException(id))
                .when(removerClienteUseCase).executar(id);

        // act + assert
        mockMvc.perform(delete("/clientes/{id}", id))
                .andExpect(status().isNotFound())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.error").value("Not Found"))
                .andExpect(jsonPath("$.message").value("Cliente não encontrado. id=" + id))
                .andExpect(jsonPath("$.path").value("/clientes/" + id));

        verify(removerClienteUseCase, times(1)).executar(id);
        verifyNoMoreInteractions(removerClienteUseCase);
    }
    
}