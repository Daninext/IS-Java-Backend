package ru.itmo.banks;

public class InvalidPercentagesBanksException extends RuntimeException {
    public InvalidPercentagesBanksException() { }

    public InvalidPercentagesBanksException(String message) {
        super(message);
    }

    public InvalidPercentagesBanksException(String message, Exception innerException) {
        super(message, innerException);
    }
}
