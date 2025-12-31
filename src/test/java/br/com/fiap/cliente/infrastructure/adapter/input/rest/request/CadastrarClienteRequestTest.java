package br.com.fiap.cliente.infrastructure.adapter.input.rest.request;

import br.com.fiap.cliente.application.dto.CadastrarClienteCommand;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class CadastrarClienteRequestTest {

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
        CadastrarClienteRequest request = new CadastrarClienteRequest();
        request.setCpf("493.566.170-40");
        request.setNome("João");
        request.setSobrenome("Silva");

        // act
        CadastrarClienteCommand cmd = request.toCommand();

        // assert
        assertNotNull(cmd);
        assertEquals("493.566.170-40", cmd.cpf());
        assertEquals("João", cmd.nome());
        assertEquals("Silva", cmd.sobrenome());
    }

    @Test
    void validation_deveFalharQuandoCpfEmBranco() {
        CadastrarClienteRequest request = requestValido();
        request.setCpf(" ");

        Set<ConstraintViolation<CadastrarClienteRequest>> violations = validator.validate(request);

        assertHasViolationForField(violations, "cpf");
    }

    @Test
    void validation_deveFalharQuandoCpfInvalido() {
        CadastrarClienteRequest request = requestValido();
        request.setCpf("493.566.170-41");

        Set<ConstraintViolation<CadastrarClienteRequest>> violations = validator.validate(request);

        assertHasViolationForField(violations, "cpf");
    }

    @Test
    void validation_deveFalharQuandoNomeEmBranco() {
        CadastrarClienteRequest request = requestValido();
        request.setNome("   ");

        Set<ConstraintViolation<CadastrarClienteRequest>> violations = validator.validate(request);

        assertHasViolationForField(violations, "nome");
    }

    @Test
    void validation_deveFalharQuandoSobrenomeEmBranco() {
        CadastrarClienteRequest request = requestValido();
        request.setSobrenome("");

        Set<ConstraintViolation<CadastrarClienteRequest>> violations = validator.validate(request);

        assertHasViolationForField(violations, "sobrenome");
    }

    private static CadastrarClienteRequest requestValido() {
        CadastrarClienteRequest request = new CadastrarClienteRequest();
        request.setCpf("493.566.170-40");
        request.setNome("João");
        request.setSobrenome("Silva");
        return request;
    }

    private static void assertHasViolationForField(
            Set<ConstraintViolation<CadastrarClienteRequest>> violations,
            String fieldName
    ) {
        assertFalse(violations.isEmpty(), "Esperava violações, mas não houve nenhuma.");

        boolean found = violations.stream()
                .anyMatch(v -> v.getPropertyPath() != null && fieldName.equals(v.getPropertyPath().toString()));

        assertTrue(found, () -> "Esperava violação no campo '" + fieldName + "', mas veio: " + violations);
    }
    
}