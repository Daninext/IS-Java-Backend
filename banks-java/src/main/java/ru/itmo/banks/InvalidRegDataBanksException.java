package ru.itmo.banks;

public class InvalidRegDataBanksException extends RuntimeException {
    public InvalidRegDataBanksException() { }

    public InvalidRegDataBanksException(String message) {
        super(message);
    }

    public InvalidRegDataBanksException(String message, Exception innerException) {
        super(message, innerException);
    }
}
