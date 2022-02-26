package ru.itmo.banks;

public class PercentNotExistsBanksException extends Exception {
    public PercentNotExistsBanksException() { }

    public PercentNotExistsBanksException(String message) {
        super(message);
    }

    public PercentNotExistsBanksException(String message, Exception innerException) {
        super(message, innerException);
    }
}
