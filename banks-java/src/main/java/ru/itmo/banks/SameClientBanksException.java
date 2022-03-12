package ru.itmo.banks;

public class SameClientBanksException extends RuntimeException {
    public SameClientBanksException() { }

    public SameClientBanksException(String message) {
        super(message);
    }

    public SameClientBanksException(String message, Exception innerException) {
        super(message, innerException);
    }
}
