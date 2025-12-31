package br.com.fiap.cliente.infrastructure.adapter.output.persistence;

import br.com.fiap.cliente.domain.model.Cliente;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class ClienteJpaEntityTest {

    @Test
    void fromDomain_deveMapearTodosOsCamposCorretamente() {
        // arrange
        UUID id = UUID.randomUUID();

        Cliente cliente = new Cliente(
                id,
                "245.701.440-98",
                "João",
                "Silva"
        );

        // act
        ClienteJpaEntity entity = ClienteJpaEntity.fromDomain(cliente);

        // assert
        assertNotNull(entity);
        assertEquals(id, getField(entity, "id"));
        assertEquals("245.701.440-98", getField(entity, "cpf"));
        assertEquals("João", getField(entity, "nome"));
        assertEquals("Silva", getField(entity, "sobrenome"));
    }

    @Test
    void toDomain_deveConverterEntityParaDominioCorretamente() {
        // arrange
        ClienteJpaEntity entity = new ClienteJpaEntity();

        setField(entity, "id", UUID.randomUUID());
        setField(entity, "cpf", "245.701.440-98");
        setField(entity, "nome","João");
        setField(entity, "sobrenome", "Silva");

        // act
        Cliente cliente = entity.toDomain();

        // assert
        assertNotNull(cliente);
        assertEquals("245.701.440-98", cliente.getCpf());
        assertEquals("João", cliente.getNome());
        assertEquals("Silva", cliente.getSobrenome());
    }

    @Test
    void roundTrip_domainParaEntityParaDomain_devePreservarDados() {
        // arrange
        Cliente original = new Cliente(
                UUID.randomUUID(),
                "245.701.440-98",
                "João",
                "Silva"
        );

        // act
        ClienteJpaEntity entity = ClienteJpaEntity.fromDomain(original);
        Cliente convertido = entity.toDomain();

        // assert
        assertEquals(original.getId(), convertido.getId());
        assertEquals(original.getCpf(), convertido.getCpf());
        assertEquals(original.getNome(), convertido.getNome());
        assertEquals(original.getSobrenome(), convertido.getSobrenome());
    }

    // ===== Helpers de reflexão (sem setters públicos) =====

    private static Object getField(Object target, String fieldName) {
        try {
            var field = target.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            return field.get(target);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static void setField(Object target, String fieldName, Object value) {
        try {
            var field = target.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(target, value);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    
}