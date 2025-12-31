package br.com.fiap.cliente.infrastructure.adapter.input.rest;

import br.com.fiap.cliente.application.port.input.AtualizarClienteUseCase;
import br.com.fiap.cliente.application.port.input.BuscarClientePorIdUseCase;
import br.com.fiap.cliente.application.port.input.CadastrarClienteUseCase;
import br.com.fiap.cliente.application.port.input.ListarClientesUseCase;
import br.com.fiap.cliente.application.port.input.RemoverClienteUseCase;
import br.com.fiap.cliente.domain.model.Cliente;
import br.com.fiap.cliente.infrastructure.adapter.input.rest.request.AtualizarClienteRequest;
import br.com.fiap.cliente.infrastructure.adapter.input.rest.request.CadastrarClienteRequest;
import br.com.fiap.cliente.infrastructure.adapter.input.rest.response.CadastrarClienteResponse;
import br.com.fiap.cliente.infrastructure.adapter.input.rest.response.ClienteResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/clientes")
@AllArgsConstructor
public class ClienteController {

    private final BuscarClientePorIdUseCase buscarClientePorId;
    private final ListarClientesUseCase listarClientes;
    private final CadastrarClienteUseCase cadastrarCliente;
    private final AtualizarClienteUseCase atualizarCliente;
    private final RemoverClienteUseCase removerCliente;

    @Operation(summary = "Busca um cliente pelo id")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Cliente encontrado"),
            @ApiResponse(responseCode = "404", description = "Cliente não encontrado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<ClienteResponse> buscarPorId(
            @Parameter(description = "Identificador do cliente", required = true)
            @PathVariable UUID id
    ) {
        var cliente = buscarClientePorId.executar(id);
        return ResponseEntity.ok(ClienteResponse.fromDomain(cliente));
    }

    @Operation(summary = "Lista clientes")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista de clientes retornada com sucesso")
    })
    @GetMapping
    public ResponseEntity<List<ClienteResponse>> listar() {
        List<Cliente> clientes = listarClientes.executar();
        List<ClienteResponse> response = clientes.stream()
                .map(ClienteResponse::fromDomain)
                .toList();

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Cadastra um novo cliente")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Cliente cadastrado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos")
    })
    @PostMapping
    public ResponseEntity<CadastrarClienteResponse> cadastrar(
            @RequestBody @Valid CadastrarClienteRequest request
    ) {
        UUID clienteId = cadastrarCliente.executar(request.toCommand());
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new CadastrarClienteResponse(clienteId));
    }

    @Operation(summary = "Atualiza um cliente existente")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Cliente atualizado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
            @ApiResponse(responseCode = "404", description = "Cliente não encontrado")
    })
    @PutMapping("/{id}")
    public ResponseEntity<Void> atualizar(
            @Parameter(description = "Identificador do cliente", required = true)
            @PathVariable UUID id,
            @RequestBody @Valid AtualizarClienteRequest request
    ) {
        atualizarCliente.executar(request.toCommand(id));
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Remove um cliente por id")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Removido com sucesso"),
            @ApiResponse(responseCode = "404", description = "Cliente não encontrado")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> remover(
            @Parameter(description = "Identificador do cliente", required = true)
            @PathVariable UUID id
    ) {
        removerCliente.executar(id);
        return ResponseEntity.noContent().build();
    }

}
