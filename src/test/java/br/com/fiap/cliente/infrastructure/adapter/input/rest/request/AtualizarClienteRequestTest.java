package br.com.fiap.cliente.infrastructure.adapter.input.rest.request;

import br.com.fiap.cliente.application.dto.AtualizarClienteCommand;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class AtualizarClienteRequestTest {

    private static ValidatorFactory validatorFactory;
    private static Validator validator;

    @BeforeAll
    static void initValidator() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    @AfterAll
    static void closeValidatorFactory() {
        validatorFactory.close();
    }

    @Test
    void toCommand_deveMapearCamposCorretamente() {
        // arrange
        UUID id = UUID.fromString("22222222-2222-2222-2222-222222222222");

        AtualizarClienteRequest request = new AtualizarClienteRequest();
        request.setCpf("493.566.170-40");
        request.setNome("João");
        request.setSobrenome("Silva");

        // act
        AtualizarClienteCommand cmd = request.toCommand(id);

        // assert
        assertNotNull(cmd);
        assertEquals(id, cmd.id());
        assertEquals("493.566.170-40", cmd.cpf());
        assertEquals("João", cmd.nome());
        assertEquals("Silva", cmd.sobrenome());
    }

    @Test
    void validation_deveFalharQuandoCpfEmBranco() {
        AtualizarClienteRequest request = requestValido();
        request.setCpf("  ");

        Set<ConstraintViolation<AtualizarClienteRequest>> violations = validator.validate(request);

        assertHasViolationForField(violations, "cpf");
    }

    @Test
    void validation_deveFalharQuandoCpfInvalido() {
        AtualizarClienteRequest request = requestValido();
        request.setCpf("493.566.170-41");

        Set<ConstraintViolation<AtualizarClienteRequest>> violations = validator.validate(request);

        assertHasViolationForField(violations, "cpf");
    }

    @Test
    void validation_deveFalharQuandoNomeEmBranco() {
        AtualizarClienteRequest request = requestValido();
        request.setNome("   ");

        Set<ConstraintViolation<AtualizarClienteRequest>> violations = validator.validate(request);

        assertHasViolationForField(violations, "nome");
    }

    @Test
    void validation_deveFalharQuandoSobrenomeEmBranco() {
        AtualizarClienteRequest request = requestValido();
        request.setSobrenome(" ");

        Set<ConstraintViolation<AtualizarClienteRequest>> violations = validator.validate(request);

        assertHasViolationForField(violations, "sobrenome");
    }

    private static AtualizarClienteRequest requestValido() {
        AtualizarClienteRequest request = new AtualizarClienteRequest();
        request.setCpf("493.566.170-40");
        request.setNome("João");
        request.setSobrenome("Silva");
        return request;
    }

    private static void assertHasViolationForField(
            Set<ConstraintViolation<AtualizarClienteRequest>> violations,
            String fieldName
    ) {
        assertFalse(violations.isEmpty(), "Esperava violações, mas não houve nenhuma.");

        boolean found = violations.stream()
                .anyMatch(v -> v.getPropertyPath() != null && fieldName.equals(v.getPropertyPath().toString()));

        assertTrue(found, () -> "Esperava violação no campo '" + fieldName + "', mas veio: " + violations);
    }

}