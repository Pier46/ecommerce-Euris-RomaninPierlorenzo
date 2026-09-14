package it.pierlorenzo.ecommerce.exception;

public class ClienteNonTrovatoException extends RuntimeException
{
    public ClienteNonTrovatoException(String message) {
        super(message);
    }
}
