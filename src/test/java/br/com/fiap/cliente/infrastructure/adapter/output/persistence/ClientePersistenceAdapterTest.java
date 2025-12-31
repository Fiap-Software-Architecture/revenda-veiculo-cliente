package br.com.fiap.cliente.infrastructure.adapter.output.persistence;

import br.com.fiap.cliente.domain.model.Cliente;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ClientePersistenceAdapterTest {

    @Mock
    private ClienteRepositoryJpa repositoryJpa;

    @InjectMocks
    private ClientePersistenceAdapter adapter;

    private Cliente clienteDominio;

    @BeforeEach
    void setUp() {
        clienteDominio = new Cliente(
                UUID.randomUUID(),
                "245.701.440-98",
                "João",
                "Silva"
        );
    }

    @Test
    void existePorCpf_deveDelegarParaRepositoryJpa() {
        // arrange
        String cpf = "245.701.440-98";
        when(repositoryJpa.existsByCpf(cpf)).thenReturn(true);

        // act
        boolean existe = adapter.existePorCpf(cpf);

        // assert
        assertTrue(existe);
        verify(repositoryJpa, times(1)).existsByCpf(cpf);
        verifyNoMoreInteractions(repositoryJpa);
    }

    @Test
    void salvar_deveConverterDominioParaEntityESalvar() {
        // arrange
        ArgumentCaptor<ClienteJpaEntity> captor =
                ArgumentCaptor.forClass(ClienteJpaEntity.class);

        when(repositoryJpa.save(any(ClienteJpaEntity.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        // act
        Cliente clienteSalvo = adapter.salvar(clienteDominio);

        // assert
        verify(repositoryJpa).save(captor.capture());
        ClienteJpaEntity entityEnviada = captor.getValue();

        assertEquals(clienteDominio.getId(), getField(entityEnviada, "id"));
        assertEquals(clienteDominio.getCpf(), getField(entityEnviada, "cpf"));
        assertEquals(clienteDominio.getNome(), getField(entityEnviada, "nome"));
        assertEquals(clienteDominio.getSobrenome(), getField(entityEnviada, "sobrenome"));

        assertNotNull(clienteSalvo);
        assertEquals(clienteDominio.getId(), clienteSalvo.getId());
        assertEquals(clienteDominio.getCpf(), clienteSalvo.getCpf());
        assertEquals(clienteDominio.getNome(), clienteSalvo.getNome());
        assertEquals(clienteDominio.getSobrenome(), clienteSalvo.getSobrenome());
    }

    @Test
    void existePorPlacaEIdDiferente_deveDelegarParaRepositoryJpa() {
        // arrange
        String cpf = "245.701.440-98";
        UUID id = UUID.fromString("11111111-1111-1111-1111-111111111111");

        when(repositoryJpa.existsByCpfAndIdNot(cpf, id)).thenReturn(true);

        // act
        boolean existe = adapter.existePorCpfEIdDiferente(cpf, id);

        // assert
        assertTrue(existe);
        verify(repositoryJpa, times(1)).existsByCpfAndIdNot(cpf, id);
        verifyNoMoreInteractions(repositoryJpa);
    }

    @Test
    void buscarPorId_quandoEncontrado_deveConverterEntityParaDominio() {
        // arrange
        UUID id = UUID.fromString("22222222-2222-2222-2222-222222222222");

        ClienteJpaEntity entity = ClienteJpaEntity.fromDomain(
                new Cliente(
                        id,
                        "245.701.440-98",
                        "João",
                        "Silva"
                )
        );

        when(repositoryJpa.findById(id)).thenReturn(Optional.of(entity));

        // act
        Optional<Cliente> result = adapter.buscarPorId(id);

        // assert
        assertTrue(result.isPresent());
        Cliente cliente = result.get();

        assertEquals(id, cliente.getId());
        assertEquals("245.701.440-98", cliente.getCpf());
        assertEquals("João", cliente.getNome());
        assertEquals("Silva", cliente.getSobrenome());

        verify(repositoryJpa, times(1)).findById(id);
        verifyNoMoreInteractions(repositoryJpa);
    }

    @Test
    void buscarPorId_quandoNaoEncontrado_deveRetornarOptionalVazio() {
        // arrange
        UUID id = UUID.fromString("33333333-3333-3333-3333-333333333333");
        when(repositoryJpa.findById(id)).thenReturn(Optional.empty());

        // act
        Optional<Cliente> result = adapter.buscarPorId(id);

        // assert
        assertTrue(result.isEmpty());
        verify(repositoryJpa, times(1)).findById(id);
        verifyNoMoreInteractions(repositoryJpa);
    }

    @Test
    void listarOrdenadoPorNome_deveDelegarParaRepositoryJpaEConverterParaDominio() {
        // arrange
        Cliente cliente1 = new Cliente(
                UUID.fromString("44444444-4444-4444-4444-444444444444"),
                "245.701.440-98",
                "João",
                "Silva"
        );

        Cliente cliente2 = new Cliente(
                UUID.fromString("55555555-5555-5555-5555-555555555555"),
                "832.380.250-58",
                "Adriano",
                "Silva"
        );

        // O adapter confia na ordenação vinda do repository
        List<ClienteJpaEntity> entitiesOrdenadas = List.of(
                ClienteJpaEntity.fromDomain(cliente2),
                ClienteJpaEntity.fromDomain(cliente1)
        );

        when(repositoryJpa.findAllByOrderByNome()).thenReturn(entitiesOrdenadas);

        // act
        List<Cliente> result = adapter.listarOrdenadoPorNome();

        // assert
        assertNotNull(result);
        assertEquals(2, result.size());

        assertEquals(cliente2.getId(), result.getFirst().getId());
        assertEquals(cliente2.getNome(), result.getFirst().getNome());
        assertEquals(cliente2.getSobrenome(), result.getFirst().getSobrenome());

        assertEquals(cliente1.getId(), result.get(1).getId());
        assertEquals(cliente1.getNome(), result.get(1).getNome());
        assertEquals(cliente1.getSobrenome(), result.get(1).getSobrenome());

        verify(repositoryJpa, times(1)).findAllByOrderByNome();
        verifyNoMoreInteractions(repositoryJpa);
    }

    @Test
    void removerPorId_deveDelegarParaRepositoryJpa() {
        // arrange
        UUID id = UUID.fromString("99999999-9999-9999-9999-999999999999");

        // act
        adapter.removerPorId(id);

        // assert
        verify(repositoryJpa, times(1)).deleteById(id);
        verifyNoMoreInteractions(repositoryJpa);
    }


    private static Object getField(Object target, String fieldName) {
        try {
            var field = target.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            return field.get(target);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    
}