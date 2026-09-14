package it.pierlorenzo.ecommerce.exception;

public class ProdottoNonTrovatoException extends RuntimeException {

    public ProdottoNonTrovatoException(String message) {
        super(message);
    }
}
