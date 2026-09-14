package it.pierlorenzo.ecommerce.exception;

public class OrdineNonTrovatoException extends RuntimeException {

    public OrdineNonTrovatoException(String message) {
        super(message);
    }
}