package br.com.fiap.cliente.infrastructure.adapter.input.rest.exception;

import br.com.fiap.cliente.domain.exception.ClienteNaoEncontradoException;
import br.com.fiap.cliente.domain.exception.CpfJaCadastradoException;
import br.com.fiap.cliente.domain.exception.DomainValidationException;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler handler = new GlobalExceptionHandler();

    @Test
    void handleBeanValidation_deveRetornar400ComFieldErrors() {
        // arrange
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getRequestURI()).thenReturn("/clientes");

        BeanPropertyBindingResult bindingResult =
                new BeanPropertyBindingResult(new Object(), "request");
        bindingResult.addError(new FieldError("request", "cpf", "não pode ser vazio"));

        MethodArgumentNotValidException ex =
                new MethodArgumentNotValidException(null, bindingResult);

        // act
        ResponseEntity<ApiErrorResponse> response = handler.handleBeanValidation(ex, request);

        // assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

        ApiErrorResponse body = response.getBody();
        assertNotNull(body);
        assertEquals(400, body.status());
        assertEquals("Erro de validação da requisição", body.message());
        assertEquals("/clientes", body.path());
        assertEquals(1, body.fieldErrors().size());
        assertEquals("cpf", body.fieldErrors().getFirst().field());
        assertEquals("não pode ser vazio", body.fieldErrors().getFirst().message());
    }

    @Test
    void handleDomainValidation_deveRetornar400() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getRequestURI()).thenReturn("/clientes");

        DomainValidationException ex = new DomainValidationException("placa", "placa inválida");

        ResponseEntity<ApiErrorResponse> response = handler.handleDomainValidation(ex, request);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

        ApiErrorResponse body = response.getBody();
        assertNotNull(body);
        assertEquals("Erro de validação no domínio", body.message());
        assertEquals("/clientes", body.path());
        assertEquals(1, body.fieldErrors().size());
        assertEquals("placa", body.fieldErrors().getFirst().field());
        assertEquals("placa inválida", body.fieldErrors().getFirst().message());
    }

    @Test
    void handleInvalidJson_deveRetornar400() {
        // arrange
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getRequestURI()).thenReturn("/clientes");

        HttpInputMessage inputMessage = new HttpInputMessage() {
            @Override
            public InputStream getBody() {
                return new ByteArrayInputStream(new byte[0]);
            }

            @Override
            public HttpHeaders getHeaders() {
                return new HttpHeaders();
            }
        };

        HttpMessageNotReadableException ex =
                new HttpMessageNotReadableException("json inválido", inputMessage);

        // act
        ResponseEntity<ApiErrorResponse> response =
                handler.handleInvalidJson(ex, request);

        // assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

        ApiErrorResponse body = response.getBody();
        assertNotNull(body);
        assertEquals(400, body.status());
        assertEquals("JSON inválido ou malformado", body.message());
        assertEquals("/clientes", body.path());
        assertTrue(body.fieldErrors().isEmpty());
    }

    @Test
    void handleCpfJaCadastrada_deveRetornar409() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getRequestURI()).thenReturn("/clientes");

        CpfJaCadastradoException ex = new CpfJaCadastradoException("493.566.170-40");

        ResponseEntity<ApiErrorResponse> response = handler.handle(ex, request);

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());

        ApiErrorResponse body = response.getBody();
        assertNotNull(body);
        assertEquals(409, body.status());
        assertEquals("Cpf já cadastrado: 493.566.170-40", body.message());
        assertEquals("/clientes", body.path());
        assertTrue(body.fieldErrors().isEmpty());
    }

    @Test
    void handleClienteNaoEncontrado_deveRetornar404() {
        // arrange
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getRequestURI()).thenReturn("/clientes/22222222-2222-2222-2222-222222222222");

        UUID id = UUID.fromString("22222222-2222-2222-2222-222222222222");
        ClienteNaoEncontradoException ex = new ClienteNaoEncontradoException(id);

        // act
        ResponseEntity<ApiErrorResponse> response =
                handler.handleClienteNaoEncontrado(ex, request);

        // assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());

        ApiErrorResponse body = response.getBody();
        assertNotNull(body);

        assertNotNull(body.timestamp());
        assertEquals(404, body.status());
        assertEquals("Not Found", body.error());
        assertEquals("Cliente não encontrado. id=" + id, body.message());
        assertEquals("/clientes/22222222-2222-2222-2222-222222222222", body.path());
        assertTrue(body.fieldErrors().isEmpty());
    }
    
}