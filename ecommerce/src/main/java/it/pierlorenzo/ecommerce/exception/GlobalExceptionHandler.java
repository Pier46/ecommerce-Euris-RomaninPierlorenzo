package it.pierlorenzo.ecommerce.exception;

import it.pierlorenzo.ecommerce.dto.ErroreResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(StockInsufficienteException.class)
    public ResponseEntity<ErroreResponse> handleStockInsufficiente(
            StockInsufficienteException exception) {

        HttpStatus status = HttpStatus.CONFLICT;

        return ResponseEntity
                .status(status)
                .body(creaErrore(status, exception.getMessage()));
    }

    @ExceptionHandler(OrdineNonTrovatoException.class)
    public ResponseEntity<ErroreResponse> handleOrdineNonTrovato(
            OrdineNonTrovatoException exception) {

        HttpStatus status = HttpStatus.NOT_FOUND;

        return ResponseEntity
                .status(status)
                .body(creaErrore(status, exception.getMessage()));
    }

    @ExceptionHandler(OrdineConsegnatoException.class)
    public ResponseEntity<ErroreResponse> handleOrdineConsegnato(
            OrdineConsegnatoException exception) {

        HttpStatus status = HttpStatus.CONFLICT;

        return ResponseEntity
                .status(status)
                .body(creaErrore(status, exception.getMessage()));
    }

    @ExceptionHandler(StatoOrdineNonValidoException.class)
    public ResponseEntity<ErroreResponse> handleStatoOrdineNonValido(
            StatoOrdineNonValidoException exception) {

        HttpStatus status = HttpStatus.CONFLICT;

        return ResponseEntity
                .status(status)
                .body(creaErrore(status, exception.getMessage()));
    }

    @ExceptionHandler(ClienteNonTrovatoException.class)
    public ResponseEntity<ErroreResponse> handleClienteNonTrovato(
            ClienteNonTrovatoException exception) {

        HttpStatus status = HttpStatus.NOT_FOUND;

        return ResponseEntity
                .status(status)
                .body(creaErrore(status, exception.getMessage()));
    }

    @ExceptionHandler(ProdottoNonTrovatoException.class)
    public ResponseEntity<ErroreResponse> handleProdottoNonTrovato(
            ProdottoNonTrovatoException exception) {

        HttpStatus status = HttpStatus.NOT_FOUND;

        return ResponseEntity
                .status(status)
                .body(creaErrore(status, exception.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErroreResponse> handleValidationError(
            MethodArgumentNotValidException exception) {

        HttpStatus status = HttpStatus.BAD_REQUEST;

        String messaggio = exception.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.joining(", "));

        return ResponseEntity
                .status(status)
                .body(creaErrore(status, messaggio));
    }

    private ErroreResponse creaErrore(
            HttpStatus status,
            String message) {

        return new ErroreResponse(
                status.value(),
                status.getReasonPhrase(),
                message,
                LocalDateTime.now()
        );
    }
}