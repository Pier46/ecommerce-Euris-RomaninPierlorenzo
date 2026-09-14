package it.pierlorenzo.ecommerce.exception;

public class StockInsufficienteException extends RuntimeException {

    public StockInsufficienteException(String message) {
        super(message);
    }
}