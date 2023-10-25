package com.transactioncurrency.infra.handler;

import com.transactioncurrency.dtos.ExceptionDTO;
import io.vertx.ext.web.handler.impl.HttpStatusException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import java.util.ArrayList;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

@RestControllerAdvice
public class ControllerExceptionHandler {

    @ExceptionHandler(HttpStatusException.class)
    public ResponseEntity threatRequestException(HttpStatusException exception) {
        ExceptionDTO exceptionDTO = new ExceptionDTO(exception.getPayload(), exception.getStatusCode());
        return ResponseEntity.status(exception.getStatusCode()).body(exceptionDTO);
    }

    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity threatNullPointerException(NullPointerException exception) {
        ExceptionDTO exceptionDTO = new ExceptionDTO(exception.getMessage(), HttpStatus.BAD_REQUEST.value());
        return ResponseEntity.badRequest().body(exceptionDTO);
    }

    @ExceptionHandler({TransactionSystemException.class})
    public ResponseEntity<Object> threatPersistenceException(final Exception ex, final WebRequest request) {
        Throwable cause = ((TransactionSystemException) ex).getRootCause();
        if (cause instanceof ConstraintViolationException) {

            ConstraintViolationException consEx = (ConstraintViolationException) cause;
            final List<String> errors = new ArrayList<>();
            for (final ConstraintViolation<?> violation : consEx.getConstraintViolations()) {
                errors.add(violation.getMessage());
            }

            final var exceptionDTO = new ExceptionDTO(
                    String.join(", ", errors), HttpStatus.BAD_REQUEST.value());
            return ResponseEntity.badRequest().body(exceptionDTO);
        }
        final var exceptionDTO = new ExceptionDTO(ex.getLocalizedMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value());
        return ResponseEntity.internalServerError().body(exceptionDTO);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity threat404(EntityNotFoundException exception) {
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity threatGeneralException(Exception exception) {
        ExceptionDTO exceptionDTO = new ExceptionDTO(exception.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value());
        return ResponseEntity.internalServerError().body(exceptionDTO);
    }
}
